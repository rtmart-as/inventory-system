package com.ychs.web.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 支付宝沙箱支付配置
 */
@Data
@Component
@ConfigurationProperties(prefix = "alipay")
public class AlipayProperties {
    /**
     * 应用ID(沙箱环境)
     */
    private String appId;
    /**
     * 应用私钥(PKCS8 PEM)
     */
    private String appPrivateKey;
    /**
     * 支付宝公钥(PEM)
     */
    private String alipayPublicKey;
    /**
     * 网关地址(沙箱: https://openapi-sandbox.dl.alipaydev.com/gateway.do)
     */
    private String gateway;
    /**
     * 异步通知地址
     */
    private String notifyUrl;
    /**
     * 同步回跳地址
     */
    private String returnUrl;
}
