package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.CommentsDto;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.repository.PostRepository;
import io.bgnc.SpringBootApplication.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    private final PostRepository postRepository;
    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) throws PostFoundNotFoundException {


        commentService.save(commentsDto);

        return new ResponseEntity<>(HttpStatus.CREATED);

    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) throws PostFoundNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForPost(postId));
    }


    @GetMapping("/by-user/{username}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String username){

        return ResponseEntity.status(HttpStatus.OK).body(commentService.getAllCommentsForUser(username));


    }

}
