package com.example.accounting.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;

/**
 * @ClassName:CategoryType
 * @Description:分类统计功能，返回模板
 * @Author:liumengying
 * @Date: 2023/4/15 23:03
 * Version v1.0
 */

@Data
public class CategoryType implements Serializable {

    private BigDecimal money; //消费金额
    private String typename;
}
