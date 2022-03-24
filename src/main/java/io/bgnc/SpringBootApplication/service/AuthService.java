package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.RegisterRequest;
import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.NotificationEmail;
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
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AuthService {

    /**
     * We don't need to write @Autowired because we added the AllArgsConstructor,
     * so it  can find easily that types or repos.
     */

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    private final VerificationTokenRepository verificationTokenRepository;


    private final MailService mailService;
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

       mailService.sendMail(new NotificationEmail("Please ! Activate your email address", user.getEmail(),"Thank you click on the below url to activate your email " +
               "http://localhost:8080/api/auth/accountVerification/"+token ));
    }

    private String generateVerificationToken(User user) {

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);

        verificationTokenRepository.save(verificationToken);
        return token;

    }

    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationToken=verificationTokenRepository.findByToken(token);

        verificationToken.orElseThrow(()-> new SpringBootApplicationException("Invalid Token"));

        fetchUserAndEnable(verificationToken.get());

    }


    private void fetchUserAndEnable(VerificationToken verificationToken) {

        String username  = verificationToken.getUser().getUsername();

        User user = userRepository.findByUsername(username).
                orElseThrow(()->new SpringBootApplicationException("Entered username could not found "+username));

        user.setEnabled(true);
        userRepository.save(user);

    }
}
