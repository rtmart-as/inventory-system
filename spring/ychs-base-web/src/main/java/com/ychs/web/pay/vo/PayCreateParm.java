package com.ychs.web.pay.vo;

import lombok.Data;

/**
 * 创建支付参数
 */
@Data
public class PayCreateParm {
    /**
     * 业务类型 1销售 2采购
     */
    private String bizType;
    /**
     * 订单id
     */
    private String orderId;
}
