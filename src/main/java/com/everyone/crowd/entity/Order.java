package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Order {
    private Integer id;
    private Integer devId;
    private Integer customerId;
    private Integer demandId;
    private Date orderTime;
    private BigDecimal price;
    private Date completeTime;
    private String status;
}
