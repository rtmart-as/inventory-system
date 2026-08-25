/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_returning.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
@TableName("purchase_returning")
public class PurchaseReturning implements Serializable {
    private static final long serialVersionUID = 1L;

    @TableId(value = "return_id", type = IdType.AUTO)
    private Integer returnId;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 商品id
     */
    private Integer goodsId;

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
     * 创建时间
     */
    private Date createTime;

    /**
     * 操作人
     */
    private String createUser;

    //供应商名称
    @TableField(exist = false)
    private String supplierName;
    //商品名称
    @TableField(exist = false)
    private String goodsName;
}
