package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.PostRequest;
import io.bgnc.SpringBootApplication.dto.PostResponse;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.exceptions.SubredditNotFoundException;
import io.bgnc.SpringBootApplication.mapper.PostMapper;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.Subreddit;
import io.bgnc.SpringBootApplication.model.User;
import io.bgnc.SpringBootApplication.repository.PostRepository;
import io.bgnc.SpringBootApplication.repository.SubredditRepository;
import io.bgnc.SpringBootApplication.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;

    private final SubredditRepository subredditRepository;
    private final AuthService authService;
    private final UserRepository userRepository;

    private final PostMapper postMapper;
    public Post save(PostRequest postRequest) throws SubredditNotFoundException {

        Subreddit subreddit = subredditRepository.findByName(postRequest.getSubredditName())
                .orElseThrow(()->new SubredditNotFoundException("Exception occured "+postRequest.getSubredditName()));

        User currentUser = authService.getCurrentUser();

        return postMapper.map(postRequest,subreddit,currentUser);


    }

    public List<PostResponse> getPostsByUsername(String username) {

        User user = userRepository.findByUsername(username).
                orElseThrow(()-> new UsernameNotFoundException(username.toString()));

       return postRepository.findByUser(user)
               .stream()
               .map(postMapper::mapToDto)
               .collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubreddit(Long id) throws SubredditNotFoundException {

        Subreddit subreddit = subredditRepository.findById(id)
                .orElseThrow(()-> new SubredditNotFoundException(id.toString()));
        List<Post> posts = postRepository.findAllBySubreddit(subreddit);
        return posts.stream().map(postMapper::mapToDto).collect(Collectors.toList());

    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) throws PostFoundNotFoundException {
        Post post = postRepository.findById(id)
                .orElseThrow(()->new PostFoundNotFoundException(id.toString()));
        return postMapper.mapToDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostResponse> getAllPosts() {
        return postRepository.findAll()
                .stream()
                .map(postMapper::mapToDto)
                .collect(Collectors.toList());

    }
}
