/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
@TableName("goods")
public class Goods implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 商品id
     */
    @TableId(value = "goods_id", type = IdType.AUTO)
    private Integer goodsId;

    /**
     * 商品名称
     */
    private String goodsName;
    /**
     * 供应商名称
     */
    @TableField(exist = false)
    private String supplierName;

    /**
     * 供应商id
     */
    private Integer supplierId;

    /**
     * 产地
     */
    private String place;

    /**
     * 描述
     */
    private String goodsDesc;

    /**
     * 销售价格
     */
    private BigDecimal salePrice;

    /**
     * 库存
     */
    private Integer stock;

    /**
     * 库存预警值
     */
    private Integer stockWarn;

    /**
     * 图片地址
     */
    private String image;

    /**
     * 规格
     */
    private String speci;

    /**
     * 0:在售 1:停售
     */
    private String status;

    /**
     * 序号
     */
    private Integer orderNum;
    /**
     * 商品编号
     */
    private String goodsCode;
}
