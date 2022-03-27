package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.VoteDto;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.service.VoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/api/votes")
public class VoteController {

    private final VoteService voteService;


    @PostMapping
    public ResponseEntity<Void> vote(@RequestBody VoteDto voteDto) throws PostFoundNotFoundException {
        voteService.vote(voteDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
