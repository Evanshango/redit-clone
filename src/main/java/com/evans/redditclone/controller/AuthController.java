package com.evans.redditclone.controller;

import com.evans.redditclone.dto.RegisterDto;
import com.evans.redditclone.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth/")
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        authService.register(registerDto);
        return new ResponseEntity<>("User registered", HttpStatus.OK);
    }
}
