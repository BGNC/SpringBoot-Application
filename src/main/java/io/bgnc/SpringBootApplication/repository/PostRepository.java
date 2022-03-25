package io.bgnc.SpringBootApplication.repository;

import io.bgnc.SpringBootApplication.dto.PostResponse;
import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.Subreddit;
import io.bgnc.SpringBootApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    List<Post> findAllBySubreddit(Subreddit subreddit);

    List<Post> findByUser(User user);

}
