package com.example.accounting.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;


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
    private String incomeTypeName;  //收入类型id
    private BigDecimal incomeMoney;  //收入金额
    private String incomeTime;  //收入时间
}
