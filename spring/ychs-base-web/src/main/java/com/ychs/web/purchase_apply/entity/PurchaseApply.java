/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_apply.entity;

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
@TableName("purchase_apply")
public class PurchaseApply implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 申请id
     */
    @TableId(value = "apply_id", type = IdType.AUTO)
    private Integer applyId;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 商品id
     */
    private Integer goodsId;

    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;

    /**
     * 采购数量
     */
    private Integer purchaseCount;

    /**
     * 状态：0：待审批 1：已审批 2：拒绝
     */
    private String status;

    /**
     * 审批备注
     */
    private String applyDesc;
    /**
     * 申请人
     */
    private String applyUser;
    /**
     * 审批人
     */
    private String approveUser;

    /**
     * 申请时间
     */
    private Date applyTime;

    /**
     * 审批时间
     */
    private Date approveTime;

    //供应商名称
    @TableField(exist = false)
    private String supplierName;
    //商品名称
    @TableField(exist = false)
    private String goodsName;
    //申请人名称
    @TableField(exist = false)
    private String nickName;
}
