package com.example.accounting.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @ClassName:SpendingCredential
 * @Description:消费凭据实体类
 * @Author:liumengying
 * @Date: 2023/3/17 11:41
 * Version v1.0
 */
@Data
public class SpendingCredential implements Serializable {
    private Long id;
    private String spendingCredentialName;
    private Long userId;
}
