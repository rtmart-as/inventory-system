/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
@TableName("sale_order")
public class SaleOrder {
    private static final long serialVersionUID = 1L;

    /**
     * 订单id
     */
    @TableId(value = "order_id", type = IdType.ASSIGN_ID)
    private String orderId;

    /**
     * 商品id
     */
    private Integer goodsId;
    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 销售金额
     */
    private BigDecimal salePrice;

    /**
     * 采购数量
     */
    private Integer saleCount;

    /**
     * 生成时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date createTime;

    /**
     * 订单确定状态 0：待确定 1：已确定
     */
    private String status;
    /**
     * 支付状态 0：未支付 1：已支付
     */
    private String payStatus;
    /**
     * 支付时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private Date payTime;
    /**
     * 销售人
     */
    private Integer saleUser;

    /**
     * 销售人姓名
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
