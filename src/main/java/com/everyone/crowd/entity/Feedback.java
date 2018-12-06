package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    private Integer id;
    private Integer userId;
    private String url;
    private String title;
    private Date submitTime;
    private String content;
    private String reply;
}
