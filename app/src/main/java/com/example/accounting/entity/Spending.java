package com.example.accounting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * @ClassName:Spending
 * @Description:消费类
 * @Author:liumengying
 * @Date: 2023/3/17 9:25
 * Version v1.0
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Spending implements Serializable {
    private Long id;  //消费记录id
    private Long userId;  //关联用户id
    private BigDecimal spendingMoney; //消费金额
    private String spendingStores; //消费商家，允许null

//    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
//    private Date spendingTime;  //消费时间
    private String spendingTime;
    private String spendingCredentialName;  //消费凭据id
    private String spendingTypeName;  //消费类型ID
}
