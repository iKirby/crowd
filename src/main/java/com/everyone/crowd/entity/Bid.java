package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class Bid {
    private  Integer id;
    private  Integer demandId;
    private  Integer devId;
    private  BigDecimal price;
    private  String msg;
    private  String attachment;
}
