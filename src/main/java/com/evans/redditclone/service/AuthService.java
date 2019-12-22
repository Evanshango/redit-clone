package com.evans.redditclone.service;

import com.evans.redditclone.dto.AuthResponseDto;
import com.evans.redditclone.dto.LoginDto;
import com.evans.redditclone.dto.RegisterDto;
import com.evans.redditclone.exceptions.RedditCloneException;
import com.evans.redditclone.model.NotificationEmail;
import com.evans.redditclone.model.User;
import com.evans.redditclone.model.VerificationToken;
import com.evans.redditclone.repository.UserRepository;
import com.evans.redditclone.repository.VerificationTokenRepository;
import com.evans.redditclone.security.JwtProvider;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final MailService mailService;
    private AuthenticationManager authenticationManager;
    private JwtProvider jwtProvider;

    @Transactional
    public void register(RegisterDto registerDto) {
        User user = new User();
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setCreated(Instant.now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.sendMail(new NotificationEmail("Please activate your account", user.getEmail(),
                "Thank you for signing up on RedditClone, Please click this link to activate your account :" +
                        " http://localhost:8000/api/auth/account-verification/" + token));
    }

    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;
    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken = verificationTokenRepository.findByToken(token);
        verificationToken.orElseThrow(() -> new RedditCloneException("Invalid token"));
        fetchAndEnableUser(verificationToken.get());
    }

    @Transactional
    public void fetchAndEnableUser(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditCloneException(
                "User with name " + username + "not found"));
        user.setEnabled(true);
        userRepository.save(user);
    }

    public AuthResponseDto login(LoginDto loginDto) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsername(),
                loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String authToken = jwtProvider.generateToken(authentication);
        return new AuthResponseDto(loginDto.getUsername(), authToken);
    }
}
