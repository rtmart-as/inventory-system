/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_return.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
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
@TableName("sale_return")
public class SaleReturn implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "return_id", type = IdType.ASSIGN_ID)
    private String returnId;
    private String orderId;

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
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 退货人id
     */
    private Integer createUser;

    /**
     * 退货描述
     */
    private String description;
    private String applyDesc;
    private String applyUser;
    /**
     * 审核状态 0：待审核 1：已审核
     */
    private String status;

    /**
     * 退货人姓名
     */
    @TableField(exist = false)
    private String nickName;

    //供应商名称
    @TableField(exist = false)
    private String supplierName;
    //商品名称
    @TableField(exist = false)
    private String goodsName;
}

