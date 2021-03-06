package io.bgnc.SpringBootApplication.service;

import io.bgnc.SpringBootApplication.dto.AuthenticationResponse;
import io.bgnc.SpringBootApplication.dto.RefreshTokenRequest;
import io.bgnc.SpringBootApplication.dto.RegisterRequest;
import io.bgnc.SpringBootApplication.dto.loginRequest;
import io.bgnc.SpringBootApplication.exceptions.SpringBootApplicationException;
import io.bgnc.SpringBootApplication.model.NotificationEmail;
import io.bgnc.SpringBootApplication.model.User;
import io.bgnc.SpringBootApplication.model.VerificationToken;
import io.bgnc.SpringBootApplication.repository.UserRepository;
import io.bgnc.SpringBootApplication.repository.VerificationTokenRepository;
import io.bgnc.SpringBootApplication.security.Jwt;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

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
    private final AuthenticationManager authenticationManager;
    private final Jwt jwt;

    private final RefreshTokenService refreshTokenService;



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


    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Jwt principal = (Jwt) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        return userRepository.findByUsername(principal.getSubject())
                .orElseThrow(() -> new UsernameNotFoundException("User name not found - " + principal.getSubject()));
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

    public AuthenticationResponse login(loginRequest loginRequest) {

        Authentication authenticate  = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()));


        SecurityContextHolder.getContext().setAuthentication(authenticate);
        String token = jwt.generateToken(authenticate);

        return AuthenticationResponse.builder()
                .authenticationToken(token)
                .refreshToken(refreshTokenService.generateRefreshToken().getToken())
                .expiresAt(Instant.now().plusMillis(jwt.getJwtExpirationInMillis()))
                .username(loginRequest.getUsername())
                .build();



    }

    public boolean isLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return !(authentication instanceof AnonymousAuthenticationToken) && authentication.isAuthenticated();
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

         refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());

         String token = jwt.generateTokenWithUsername(refreshTokenRequest.getUsername());


         return AuthenticationResponse.builder()
                 .authenticationToken(token)
                 .refreshToken(refreshTokenRequest.getRefreshToken())
                 .expiresAt(Instant.now().plusMillis(jwt.getJwtExpirationInMillis()))
                 .username(refreshTokenRequest.getUsername())
                 .build();

    }
}
