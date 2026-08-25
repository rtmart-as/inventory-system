package com.ychs.web.sale_return.entity;

import lombok.Data;

@Data
public class SaleReturnParm {
    //当前第几页
    private Long currentPage;
    //每页查询的条数
    private Long pageSize;
    //退货人
    private String createUser;
    //商品名称
    private String goodsName;
    //退货人姓名
    private String nickName;
    private String supplierName;
}