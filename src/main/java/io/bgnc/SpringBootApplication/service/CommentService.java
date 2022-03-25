package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.CommentsDto;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.repository.PostRepository;
import io.bgnc.SpringBootApplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final AuthService authService;
    public void save(CommentsDto commentsDto) throws PostFoundNotFoundException {

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()->new PostFoundNotFoundException(commentsDto.getPostId().toString()));



    }
}
