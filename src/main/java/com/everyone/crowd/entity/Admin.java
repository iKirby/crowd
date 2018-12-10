package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Admin {
    private Integer id;
    private String username;
    private String email;
    private String password;
    private String twoFactor;
    private String cookie;
}
