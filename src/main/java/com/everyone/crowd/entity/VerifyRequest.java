package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class VerifyRequest {
    private Integer id;
    private Integer userId;
    private String type;
    private String realName;
    private String certType;
    private String certNo;
    private String msg;
    private String attachment;
    private Boolean processed;
}
