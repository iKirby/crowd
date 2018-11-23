package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class User {
    private Integer id;
    private String username;
    private String password;
    private String email;
    private BigDecimal balance;
    private String twoFactor;
    private String activateCode;
    private boolean activated;
    private String cookie;
}
