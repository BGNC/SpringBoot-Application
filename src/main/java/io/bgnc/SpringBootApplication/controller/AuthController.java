package io.bgnc.SpringBootApplication.controller;

import io.bgnc.SpringBootApplication.dto.AuthenticationResponse;
import io.bgnc.SpringBootApplication.dto.RefreshTokenRequest;
import io.bgnc.SpringBootApplication.dto.RegisterRequest;
import io.bgnc.SpringBootApplication.dto.loginRequest;
import io.bgnc.SpringBootApplication.service.AuthService;
import io.bgnc.SpringBootApplication.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {


    /**
     * Then you have to try that on Postman !
     *
     */
    private final AuthService authService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest){

        authService.signup(registerRequest);
        return new ResponseEntity<>("New user registration succeed",
                HttpStatus.OK);

    }

    /**
     * get user token , so we have to use {variableName}
     */
    @GetMapping("accountVerification/{token}")
    public ResponseEntity<String> verificationAccount(@PathVariable String token){
        authService.verifyAccount(token);
        return new ResponseEntity<>("User activated successfully",HttpStatus.OK);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody loginRequest loginRequest){
        return authService.login(loginRequest);

    }

    @PostMapping("/refresh/token")
    public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){

        return authService.refreshToken(refreshTokenRequest);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest){
        refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());

        return ResponseEntity.status(HttpStatus.OK)
                .body("Refresh token is deleted successfully");


    }



}
