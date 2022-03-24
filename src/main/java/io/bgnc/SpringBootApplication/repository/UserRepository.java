package io.bgnc.SpringBootApplication.repository;

import io.bgnc.SpringBootApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

}
