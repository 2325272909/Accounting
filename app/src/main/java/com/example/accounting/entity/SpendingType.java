package com.example.accounting.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName:SpendingType
 * @Description:消费类型 类
 * @Author:liumengying
 * @Date: 2023/3/17 9:38
 * Version v1.0
 */
@Data
public class SpendingType implements Serializable {
    private Long id;
    private String spendingTypeName;  //消费类型名称
    private Long userId;   //关联用户ID
}
