package com.ychs.web.purchase_order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseTotal {
    private Integer goodsId;
    private Integer purchaseCount;
    private BigDecimal purchasePrice;
    private String purchaseMonth;
    private String goodsName;
}