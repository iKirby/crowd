package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CustomerProfile {
    private Integer id;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private String addr;
    private String alipay;
    private String bio;
    private int level;
    private String cert;
    private String status;


}
