package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.PostRequest;
import io.bgnc.SpringBootApplication.dto.PostResponse;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.exceptions.SubredditNotFoundException;
import io.bgnc.SpringBootApplication.service.PostService;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@AllArgsConstructor

public class PostController {


    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) throws SubredditNotFoundException {
        postService.save(postRequest);
        /**
         * CREATED = 201
         *
         */
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) throws PostFoundNotFoundException {

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getPost(id));

    }
    @GetMapping("/")
    public ResponseEntity<List<PostResponse>> getAllPosts(){

        return ResponseEntity.status(HttpStatus.OK)
                .body(postService.getAllPosts());


    }
    @GetMapping("/by-subreddit/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubreddit(Long id) throws SubredditNotFoundException {

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsBySubreddit(id));

    }


    @GetMapping("/by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(String username){

        return ResponseEntity.status(HttpStatus.OK).body(postService.getPostsByUsername(username));

    }



}
