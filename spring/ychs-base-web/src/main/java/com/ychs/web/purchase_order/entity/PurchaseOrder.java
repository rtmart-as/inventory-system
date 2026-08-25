package com.ychs.web.purchase_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@TableName("purchase_order")
public class PurchaseOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "order_id", type = IdType.AUTO)
    private Integer orderId;

    /**
     * 采购申请id
     */
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
     * 生成时间
     */
    private Date createTime;

    /**
     * 状态 0：未入库 1：已入库
     */
    private String status;

    /**
     * 支付状态 0：未支付 1：已支付
     */
    private String payStatus;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 批号
     */
    private String batchNum;

    /**
     * 入库验货人
     */
    private String purchaseUser;

    //供应商名称
    @TableField(exist = false)
    private String supplierName;
    //商品名称
    @TableField(exist = false)
    private String goodsName;


}
