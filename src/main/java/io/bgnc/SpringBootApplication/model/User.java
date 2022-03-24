package io.bgnc.SpringBootApplication.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

import javax.persistence.Id;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

import java.time.Instant;

import static javax.persistence.GenerationType.IDENTITY;


@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data

public class User {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long userId;
    @NotBlank(message = "username is required . Please enter your username ")
    private String username;
    @NotBlank(message = "user password  is required . Please enter your password ")
    private String password;
    @Email
    @NotEmpty
    private String email;
    private Instant created;
    private boolean enabled;



}