package com.wallet.security.dto;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class JwtAuthenticationDTO {

    @NotBlank(message = "Informe um email")
    private String email;

    @NotBlank(message = "Informe uma senha")
    private String password;
}