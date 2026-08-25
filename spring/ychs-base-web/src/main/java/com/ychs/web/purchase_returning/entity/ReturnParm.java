/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_returning.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
public class ReturnParm {
    /**
     * 采购单id
     */
    private Integer orderId;
     /**
     * 退货金额
     */
    private BigDecimal returnPrice;

    /**
     * 退货数量
     */
    private Integer returnCount;

    /**
     * 批号
     */
    private String batchNum;
     /**
     * 操作人
     */
    private String createUser;
}