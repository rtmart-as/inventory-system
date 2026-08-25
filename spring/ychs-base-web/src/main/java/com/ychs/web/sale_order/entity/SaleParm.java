/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_order.entity;

import lombok.Data;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
public class SaleParm {
    //当前第几页
    private Long currentPage;
    //没有查询的条数
    private Long pageSize;
    //供应商名称
    private String supplierName;
    //商品名称
    private String goodsName;
    //销售人姓名
    private String nickName;
}
