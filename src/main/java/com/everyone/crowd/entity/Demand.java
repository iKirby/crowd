package com.everyone.crowd.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
public class Demand {
    private Integer id;
    private Integer customerId;
    private String title;
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private Date publishTime;
    private Integer categoryId;
    private String region;
    private BigDecimal price;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;
    private String detail;
    private String attachment;
    private String status;
}
