package io.bgnc.SpringBootApplication.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class RefreshTokenRequest {

    @NotBlank
    private String refreshToken;
    private String username;
}
