package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderComment {
    private Integer orderId;
    private String devComment;
    private String customerComment;
}
