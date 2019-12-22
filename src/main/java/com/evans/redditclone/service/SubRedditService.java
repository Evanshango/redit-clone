package com.evans.redditclone.service;

import com.evans.redditclone.dto.SubRedditDto;
import com.evans.redditclone.model.SubReddit;
import com.evans.redditclone.repository.SubRedditRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class SubRedditService {

    private final SubRedditRepository subRedditRepository;

    @Transactional
    public SubRedditDto saveSubReddit(SubRedditDto subRedditDto){
       SubReddit toSave = subRedditRepository.save(mapSubRedditToDto(subRedditDto));
       subRedditDto.setId(toSave.getId());
       return subRedditDto;
    }

    @Transactional
    public List<SubRedditDto> getAllSubReddits() {
        return subRedditRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    private SubRedditDto mapToDto(SubReddit subReddit) {
        return SubRedditDto.builder()
                .id(subReddit.getId())
                .name(subReddit.getName())
                .noOfPosts(subReddit.getPosts().size())
                .build();
    }

    private SubReddit mapSubRedditToDto(SubRedditDto subRedditDto) {
        return SubReddit.builder().name(subRedditDto.getName())
                .description(subRedditDto.getDescription())
                .build();
    }
}
