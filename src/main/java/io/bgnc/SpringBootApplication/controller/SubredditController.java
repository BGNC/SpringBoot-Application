package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.SubredditDto;
import io.bgnc.SpringBootApplication.service.SubredditService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subreddit")

// log.info / log.warn
@Slf4j

@AllArgsConstructor
public class SubredditController {


    private final SubredditService subredditService;


    @PostMapping
    public void createSubreddit(@RequestBody SubredditDto subredditDto){

        ResponseEntity.status(HttpStatus.CREATED).body(subredditService.save(subredditDto));



    }

    @GetMapping
    public ResponseEntity<List<SubredditDto>> getAllSubreddits(){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getAll());

    }

    @GetMapping("/{id}")
    public ResponseEntity<SubredditDto> getSubreddit(@PathVariable Long id){
        return ResponseEntity.status(HttpStatus.OK).body(subredditService.getSubreddit(id));
    }
}
