package com.example.accounting.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @ClassName:Spending
 * @Description:消费类
 * @Author:liumengying
 * @Date: 2023/3/17 9:25
 * Version v1.0
 */
@Data
public class Spending implements Serializable {
    private Long id;  //消费记录id
    private Long userId;  //关联用户id
    private BigDecimal spendingMoney; //消费金额
    private String spending_stores; //消费商家，允许null
    private LocalDateTime spending_time;  //消费时间
    private Long spendingCredentialId;  //消费凭据id
    private Long spendingTypeId;  //消费类型ID
}
