package com.ychs.web.pay.vo;

import lombok.Data;

/**
 * 创建支付返回
 */
@Data
public class PayCreateVo {
    /**
     * 支付宝收银台跳转地址
     */
    private String url;
}
