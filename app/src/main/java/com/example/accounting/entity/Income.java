package com.example.accounting.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName:Income
 * @Description:收入实体类
 * @Author:liumengying
 * @Date: 2023/3/17 16:04
 * Version v1.0
 */
@Data
public class Income implements Serializable {
    private Long id;
    private Long userId;
    private Long incomeTypeId;  //收入类型id
    private BigDecimal incomeMoney;  //收入金额
    private LocalDateTime incomeTime;  //收入时间
}
