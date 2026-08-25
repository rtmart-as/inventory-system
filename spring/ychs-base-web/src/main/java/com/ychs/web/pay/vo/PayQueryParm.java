package com.ychs.web.pay.vo;

import lombok.Data;

/**
 * 查询支付状态参数
 */
@Data
public class PayQueryParm {
    /**
     * 业务类型 1销售 2采购
     */
    private String bizType;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 是否主动调用支付宝查询交易状态(默认false,true 时穿透异步通知失效也可兜底)
     */
    private Boolean activeQuery;
}
