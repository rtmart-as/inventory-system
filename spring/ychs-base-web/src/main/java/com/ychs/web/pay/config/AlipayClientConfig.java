package com.ychs.web.pay.config;

import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 支付宝客户端 Bean 配置
 */
@Configuration
public class AlipayClientConfig {

    @Bean
    public AlipayClient alipayClient(AlipayProperties props) throws Exception {
        AlipayConfig cfg = new AlipayConfig();
        cfg.setServerUrl(props.getGateway());
        cfg.setAppId(props.getAppId());
        cfg.setPrivateKey(props.getAppPrivateKey());
        cfg.setAlipayPublicKey(props.getAlipayPublicKey());
        cfg.setFormat(AlipayConstants.FORMAT_JSON);
        cfg.setCharset(AlipayConstants.CHARSET_UTF8);
        cfg.setSignType(AlipayConstants.SIGN_TYPE_RSA2);
        return new DefaultAlipayClient(cfg);
    }
}
