package com.ychs.web.pay.controller;

import com.ychs.web.pay.service.PayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付宝回调(路径已加入 ignore.url 白名单,无 token)
 */
@RestController
@RequestMapping("/alipay")
public class AlipayCallbackController {
    @Autowired
    private PayService payService;

    /**
     * 异步通知:返回明文 success/failure,支付宝会重试直到 success
     */
    @PostMapping(value = "/notify", produces = "text/plain;charset=UTF-8")
    public String notify(HttpServletRequest request) {
        return payService.handleAlipayNotify(request);
    }

    /**
     * 同步回跳:简单结果页
     */
    @GetMapping("/return")
    public void retur(HttpServletRequest request, HttpServletResponse response) throws Exception {
        payService.handleAlipayReturn(request, response);
    }
}
