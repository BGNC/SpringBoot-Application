package io.bgnc.SpringBootApplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class AuthenticationResponse {
    private String username;
    private String refreshToken;
    private String authenticationToken;
    private Instant expiresAt;
}
