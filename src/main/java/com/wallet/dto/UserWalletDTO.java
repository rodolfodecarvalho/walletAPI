package com.wallet.dto;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserWalletDTO {

    private Long id;

    @NotNull(message = "Informe o id do usuário.")
    private Long users;

    @NotNull(message = "Informe o id do carteira.")
    private Long wallet;
}