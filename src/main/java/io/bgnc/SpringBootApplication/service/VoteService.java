package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.VoteDto;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.Vote;
import io.bgnc.SpringBootApplication.repository.PostRepository;
import io.bgnc.SpringBootApplication.repository.VoteRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static io.bgnc.SpringBootApplication.model.VoteType.UPVOTE;

@Service
@AllArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;
    private final AuthService authService;
    private final PostRepository postRepository;


    @Transactional
    public void vote(VoteDto voteDto) throws PostFoundNotFoundException {
        Post post = postRepository.findById(voteDto.getPostId())
                .orElseThrow(()-> new PostFoundNotFoundException("Post not found with Id : "+ voteDto.getPostId()));

        Optional<Vote> voteByPostAndUser = voteRepository.findTopByPostAndUserOrderByVoteIdDesc(post,authService.getCurrentUser());

        if(voteByPostAndUser.isPresent()
        && voteByPostAndUser
                .get()
                .getVoteType()
                .equals(voteDto.getVoteType())){
            throw new SpringBootApplicationException("already "+voteDto.getVoteType()+"for this post");

        }
        if(UPVOTE.equals(voteDto.getVoteType()))
            post.setVoteCount(post.getVoteCount()+1);
        else
            post.setVoteCount(post.getVoteCount()-1);

        voteRepository.save(mapToVote(voteDto,post));
        postRepository.save(post);
    }

    private Vote mapToVote(VoteDto voteDto, Post post) {
        return Vote.builder()
                .voteType(voteDto.getVoteType())
                .post(post)
                .user(authService.getCurrentUser())
                .build();
    }
}
