package io.bgnc.SpringBootApplication.repository;

import io.bgnc.SpringBootApplication.model.Post;
import io.bgnc.SpringBootApplication.model.User;
import io.bgnc.SpringBootApplication.model.Vote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote,Long> {
    Optional<Vote> findTopByPostAndUserOrderByVoteIdDesc(Post post, User currentUser);

}
