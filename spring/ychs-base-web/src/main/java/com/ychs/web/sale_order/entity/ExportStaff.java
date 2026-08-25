package com.ychs.web.sale_order.entity;

import cn.afterturn.easypoi.excel.annotation.Excel;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ExportStaff {
    @Excel(name = "姓名",orderNum = "1", width=30)
    private String nickName;
    @Excel(name = "销售总价",orderNum = "3", width=30)
    private BigDecimal salePrice;
    @Excel(name = "统计时间",orderNum = "4", width=30)
    private String saleMonth;
}