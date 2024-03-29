package com.wallet.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class WalletDTO {

    private Long id;

    @NotNull(message = "O nome não pode ser nulo.")
    @Length(min = 3, message = "O nome deve conter no mímino 3 caracteres.")
    private String name;

    @NotNull(message = "Insira um valor para carteira.")
    private BigDecimal value;
}