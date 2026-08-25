package com.ychs.web.sale_order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SubOrderParm {
    private String orderId;
    private BigDecimal salePrice;
    private Integer saleCount;
}