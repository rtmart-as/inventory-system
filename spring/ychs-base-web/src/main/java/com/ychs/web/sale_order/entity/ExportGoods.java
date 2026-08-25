package com.ychs.web.sale_order.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportGoods {
    @Excel(name = "商品名称",orderNum = "1", width=30)
    private String goodsName;
    @Excel(name = "商品数量",orderNum = "2", width=30)
    private Integer saleCount;
    @Excel(name = "商品总价",orderNum = "3", width=30)
    private BigDecimal salePrice;
    @Excel(name = "统计时间",orderNum = "4", width=30)
    private String saleMonth;
}