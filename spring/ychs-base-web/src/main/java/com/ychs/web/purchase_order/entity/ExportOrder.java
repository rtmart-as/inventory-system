package com.ychs.web.purchase_order.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportOrder {
    @Excel(name = "采购商品",orderNum = "1", width=30)
    private String goodsName;
    @Excel(name = "采购数量",orderNum = "2", width=30)
    private Integer purchaseCount;
    @Excel(name = "采购总价",orderNum = "3", width=30)
    private BigDecimal purchasePrice;
    @Excel(name = "采购时间",orderNum = "4", width=30)
    private String purchaseMonth;
}
