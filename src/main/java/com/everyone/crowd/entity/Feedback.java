package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Feedback {
    private Integer id;
    private Integer userId;
    private Integer demandId;
    private String title;
    private String content;
    private String reply;
}