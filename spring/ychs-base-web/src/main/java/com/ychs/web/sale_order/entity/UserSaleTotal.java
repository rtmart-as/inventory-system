package com.ychs.web.sale_order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserSaleTotal {
    private String userId;
    private String nickName;
    private String saleMonth;
    private BigDecimal salePrice;
}