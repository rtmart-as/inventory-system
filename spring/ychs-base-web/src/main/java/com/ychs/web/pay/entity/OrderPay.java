package com.ychs.web.pay.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付宝支付流水
 */
@Data
@TableName("order_pay")
public class OrderPay {
    /**
     * 支付流水主键
     */
    @TableId(value = "pay_id", type = IdType.AUTO)
    private Long payId;
    /**
     * 业务类型 1销售 2采购
     */
    private String bizType;
    /**
     * 业务订单号(订单id)
     */
    private String bizNo;
    /**
     * 商户订单号(支付宝out_trade_no)
     */
    private String outTradeNo;
    /**
     * 支付宝交易号
     */
    private String tradeNo;
    /**
     * 支付金额(元)
     */
    private BigDecimal totalAmount;
    /**
     * 0待支付 1已支付 2已关闭
     */
    private String payStatus;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 异步通知原文(JSON)
     */
    private String notifyJson;
}
