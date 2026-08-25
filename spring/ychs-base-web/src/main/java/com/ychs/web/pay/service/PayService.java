package com.ychs.web.pay.service;

import com.ychs.web.pay.vo.PayCreateVo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.Map;

/**
 * 支付服务
 */
public interface PayService {
    /**
     * 创建支付,返回收银台跳转URL
     *
     * @param bizType 业务类型 1销售 2采购
     * @param orderId 订单id
     */
    PayCreateVo createPay(String bizType, String orderId);

    /**
     * 查询支付状态
     *
     * @param bizType     业务类型 1销售 2采购
     * @param orderId     订单id
     * @param activeQuery true 时主动调用支付宝查询交易状态
     */
    Map<String, Object> queryPay(String bizType, String orderId, boolean activeQuery);

    /**
     * 处理支付宝异步通知,返回明文 success/failure
     */
    String handleAlipayNotify(HttpServletRequest request);

    /**
     * 同步回跳(仅展示,不修改业务数据)
     */
    void handleAlipayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
