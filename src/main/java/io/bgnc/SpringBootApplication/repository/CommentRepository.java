package io.bgnc.SpringBootApplication.repository;

import io.bgnc.SpringBootApplication.model.Comment;
import io.bgnc.SpringBootApplication.model.Post;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    MutablePropertyValues findByPost(Post post);
}
