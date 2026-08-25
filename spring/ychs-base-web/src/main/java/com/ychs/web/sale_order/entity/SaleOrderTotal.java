package com.ychs.web.sale_order.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SaleOrderTotal {
    private String goodsName;
    private String saleMonth;
    private BigDecimal salePrice;
    private Integer saleCount;
}