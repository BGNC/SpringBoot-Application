package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.PostRequest;
import io.bgnc.SpringBootApplication.dto.PostResponse;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.exceptions.SubredditNotFoundException;
import io.bgnc.SpringBootApplication.service.PostService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
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
    public List<PostResponse> getPostsBySubreddit(Long id) throws SubredditNotFoundException {

        return postService.getPostsBySubreddit(id);

    }


    @GetMapping("/by-user/{name}")
    public List<PostResponse> getPostsByUsername(String username){

        return postService.getPostsByUsername(username);

    }



}
