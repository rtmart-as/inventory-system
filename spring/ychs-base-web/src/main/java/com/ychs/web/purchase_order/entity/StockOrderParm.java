package com.ychs.web.purchase_order.entity;

import lombok.Data;

@Data
public class StockOrderParm {
    private Integer orderId;
    private Integer goodsCount;
}