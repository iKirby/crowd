package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Demand {
    private Integer id;
    private Integer customerId;
    private String title;
    private Date publishTime;
    private Integer categoryId;
    private String region;
    private BigDecimal price;
    private Date startDate;
    private Date endDate;
    private String detail;
    private String attachment;
    private String status;
}
