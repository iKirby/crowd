package com.everyone.crowd.entitiy;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DevProfile {
    private Integer userId;
    private String name;
    private String email;
    private String phone;
    private String photo;
    private String addr;
    private String alipay;
    private String bio;
    private Integer level;
    private String cert;
    private String status;
}
