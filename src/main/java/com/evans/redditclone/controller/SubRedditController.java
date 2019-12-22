package com.evans.redditclone.controller;

import com.evans.redditclone.dto.SubRedditDto;
import com.evans.redditclone.service.SubRedditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sub-reddits/")
@AllArgsConstructor
@Slf4j
public class SubRedditController {

    private final SubRedditService subRedditService;

    @PostMapping("add")
    public ResponseEntity<SubRedditDto> createSubReddit(@RequestBody SubRedditDto subRedditDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(subRedditService.saveSubReddit(subRedditDto));
    }

    @GetMapping("all")
    public ResponseEntity<List<SubRedditDto>> getAllSubReddits(){
        return ResponseEntity.status(HttpStatus.OK).body(subRedditService.getAllSubReddits());
    }
}
