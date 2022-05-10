package com.wallet.entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.wallet.util.enums.RoleEnum;

import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1693850165739564098L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String password;

    @NotNull
    private String email;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RoleEnum role;
}