package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.RegisterRequest;
import io.bgnc.SpringBootApplication.model.User;
import io.bgnc.SpringBootApplication.model.VerificationToken;
import io.bgnc.SpringBootApplication.repository.UserRepository;
import io.bgnc.SpringBootApplication.repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    @Autowired

    private final PasswordEncoder passwordEncoder;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final VerificationTokenRepository verificationTokenRepository;

    @Transactional
    public void signup(RegisterRequest registerRequest){

        User user = new User();
        user.setUsername(registerRequest.getUsername());
        /**
         * BCryptencoding
         */
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setEmail(registerRequest.getEmail());

        user.setCreated(Instant.now());
        user.setEnabled(false);

        /**
         * Adding to database
         */
        userRepository.save(user);

       String token =  generateVerificationToken(user);

    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

    }
}
