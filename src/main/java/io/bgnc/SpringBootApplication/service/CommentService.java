package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.CommentsDto;
import io.bgnc.SpringBootApplication.exceptions.PostFoundNotFoundException;
import io.bgnc.SpringBootApplication.mapper.CommentMapper;
import io.bgnc.SpringBootApplication.model.Comment;
import io.bgnc.SpringBootApplication.model.NotificationEmail;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.User;
import io.bgnc.SpringBootApplication.repository.CommentRepository;
import io.bgnc.SpringBootApplication.repository.PostRepository;
import io.bgnc.SpringBootApplication.repository.UserRepository;
import lombok.AllArgsConstructor;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@RequiredArgsConstructor
public class CommentService {

    private static final String postUrl="";
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    private final MailContentBuilder mailContentBuilder;
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final AuthService authService;
    private final MailService mailService;

    public void save(CommentsDto commentsDto) throws PostFoundNotFoundException {

        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(()->new PostFoundNotFoundException(commentsDto.getPostId().toString()));

        Comment comment = commentMapper.map(commentsDto,post,authService.getCurrentUser());

        commentRepository.save(comment);

        String message = mailContentBuilder.build(post.getUser().getUsername()+"posted"+postUrl);
        sendCommentNotification(message,post.getUser());



    }

    private void sendCommentNotification(String message, User user ){

        mailService.sendMail(new NotificationEmail(user.getUsername()+"Comment on your sharing something ",user.getEmail(),message));

    }


    public List<CommentsDto> getAllCommentsForPost(Long postId) throws PostFoundNotFoundException {

        Post post = postRepository.findById(postId)
                .orElseThrow(()->  new PostFoundNotFoundException("Exceptipon handling there is no post "+postId.toString()));

        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(Collectors.toList());


    }


    public void getAllCommentsForUser(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(()->)

    }
}
