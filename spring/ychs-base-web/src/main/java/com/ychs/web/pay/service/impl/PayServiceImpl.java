package com.ychs.web.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.domain.AlipayTradePagePayModel;
import com.alipay.api.domain.AlipayTradeQueryModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradePagePayResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ychs.web.pay.config.AlipayProperties;
import com.ychs.web.pay.entity.OrderPay;
import com.ychs.web.pay.mapper.OrderPayMapper;
import com.ychs.web.pay.service.PayService;
import com.ychs.web.pay.vo.PayCreateVo;
import com.ychs.web.purchase_order.entity.PurchaseOrder;
import com.ychs.web.purchase_order.service.PurchaseOrderService;
import com.ychs.web.sale_order.entity.SaleOrder;
import com.ychs.web.sale_order.service.SaleOrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付服务实现(支付宝电脑网站支付)
 */
@Slf4j
@Service
public class PayServiceImpl implements PayService {
    /**
     * 业务类型:销售收款
     */
    private static final String BIZ_TYPE_SALE = "1";
    /**
     * 业务类型:采购付款
     */
    private static final String BIZ_TYPE_PURCHASE = "2";
    /**
     * 流水状态:待支付
     */
    private static final String PAY_WAIT = "0";
    /**
     * 流水状态:已支付
     */
    private static final String PAY_SUCCESS = "1";
    /**
     * 流水状态:已关闭
     */
    private static final String PAY_CLOSED = "2";

    @Autowired
    private AlipayProperties alipayProperties;
    @Autowired
    private AlipayClient alipayClient;
    @Autowired
    private OrderPayMapper orderPayMapper;
    @Autowired
    private PurchaseOrderService purchaseOrderService;
    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    @Transactional
    public PayCreateVo createPay(String bizType, String orderId) {
        BigDecimal amount;
        String subject;
        if (BIZ_TYPE_PURCHASE.equals(bizType)) {
            PurchaseOrder order = purchaseOrderService.getById(Integer.valueOf(orderId));
            if (order == null) {
                throw new RuntimeException("采购订单不存在");
            }
            if (PAY_SUCCESS.equals(order.getPayStatus())) {
                throw new RuntimeException("该采购订单已付款");
            }
            amount = order.getPurchasePrice().multiply(BigDecimal.valueOf(order.getPurchaseCount()))
                    .setScale(2, RoundingMode.HALF_UP);
            subject = "采购订单付款-" + orderId;
        } else if (BIZ_TYPE_SALE.equals(bizType)) {
            SaleOrder order = saleOrderService.getById(orderId);
            if (order == null) {
                throw new RuntimeException("销售订单不存在");
            }
            if (!"1".equals(order.getStatus())) {
                throw new RuntimeException("销售订单未确定,无法收款");
            }
            if (PAY_SUCCESS.equals(order.getPayStatus())) {
                throw new RuntimeException("该销售订单已收款");
            }
            amount = order.getSalePrice().multiply(BigDecimal.valueOf(order.getSaleCount()))
                    .setScale(2, RoundingMode.HALF_UP);
            subject = "销售订单收款-" + orderId;
        } else {
            throw new RuntimeException("非法的业务类型");
        }

        String outTradeNo = buildOutTradeNo(bizType, orderId);

        //幂等:获取或创建流水(每订单一行)
        OrderPay pay = selectByBiz(bizType, orderId);
        if (pay == null) {
            pay = new OrderPay();
            pay.setBizType(bizType);
            pay.setBizNo(orderId);
            pay.setOutTradeNo(outTradeNo);
            pay.setTotalAmount(amount);
            pay.setPayStatus(PAY_WAIT);
            pay.setCreateTime(new Date());
            try {
                orderPayMapper.insert(pay);
            } catch (DuplicateKeyException e) {
                //并发插入,重读
                pay = selectByBiz(bizType, orderId);
            }
        } else if (PAY_SUCCESS.equals(pay.getPayStatus())) {
            throw new RuntimeException("该订单已支付");
        } else if (PAY_CLOSED.equals(pay.getPayStatus())) {
            //已关闭的单可复用同一 out_trade_no 重新发起
            pay.setPayStatus(PAY_WAIT);
            pay.setTradeNo(null);
            pay.setPayTime(null);
            pay.setNotifyJson(null);
            orderPayMapper.updateById(pay);
        }

        //构造电脑网站支付请求(GET 模式返回跳转URL)
        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
        model.setOutTradeNo(outTradeNo);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setTotalAmount(amount.toPlainString());
        model.setSubject(subject);
        model.setTimeExpire(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                .format(new Date(System.currentTimeMillis() + 30 * 60 * 1000L)));

        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setBizModel(model);
        request.setNotifyUrl(alipayProperties.getNotifyUrl());
        request.setReturnUrl(alipayProperties.getReturnUrl());

        try {
            AlipayTradePagePayResponse resp = alipayClient.pageExecute(request, "GET");
            PayCreateVo vo = new PayCreateVo();
            //GET 模式下 body 即完整收银台 URL
            vo.setUrl(resp.getBody());
            return vo;
        } catch (AlipayApiException e) {
            log.error("创建支付宝支付失败", e);
            throw new RuntimeException("调用支付宝创建支付失败:" + e.getErrMsg());
        }
    }

    @Override
    public Map<String, Object> queryPay(String bizType, String orderId, boolean activeQuery) {
        Map<String, Object> result = new HashMap<>();
        OrderPay pay = selectByBiz(bizType, orderId);
        if (pay == null) {
            result.put("payStatus", PAY_WAIT);
            result.put("tradeNo", null);
            result.put("payTime", null);
            return result;
        }
        if (PAY_SUCCESS.equals(pay.getPayStatus())) {
            result.put("payStatus", PAY_SUCCESS);
            result.put("tradeNo", pay.getTradeNo());
            result.put("payTime", pay.getPayTime());
            return result;
        }
        if (activeQuery && PAY_WAIT.equals(pay.getPayStatus())) {
            AlipayTradeQueryResponse qr = queryAlipayTrade(pay.getOutTradeNo());
            if (qr != null && ("TRADE_SUCCESS".equals(qr.getTradeStatus())
                    || "TRADE_FINISHED".equals(qr.getTradeStatus()))) {
                markPaid(pay, qr.getTradeNo(), qr.getSendPayDate());
                result.put("payStatus", PAY_SUCCESS);
            } else if (qr != null && "TRADE_CLOSED".equals(qr.getTradeStatus())) {
                markClosed(pay);
                result.put("payStatus", PAY_CLOSED);
            } else {
                result.put("payStatus", PAY_WAIT);
            }
        } else {
            result.put("payStatus", pay.getPayStatus());
        }
        result.put("tradeNo", pay.getTradeNo());
        result.put("payTime", pay.getPayTime());
        return result;
    }

    @Override
    @Transactional
    public String handleAlipayNotify(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((k, v) -> params.put(k, v.length > 0 ? v[0] : ""));
        try {
            boolean signOk = AlipaySignature.rsaCheckV1(params,
                    alipayProperties.getAlipayPublicKey(), "UTF-8", AlipayConstants.SIGN_TYPE_RSA2);
            if (!signOk) {
                return "failure";
            }
            if (!alipayProperties.getAppId().equals(params.get("app_id"))) {
                return "failure";
            }

            String outTradeNo = params.get("out_trade_no");
            String tradeStatus = params.get("trade_status");
            OrderPay pay = orderPayMapper.selectOne(new LambdaQueryWrapper<OrderPay>()
                    .eq(OrderPay::getOutTradeNo, outTradeNo));
            if (pay == null) {
                return "failure";
            }

            //金额校验
            if (params.get("total_amount") != null
                    && pay.getTotalAmount().compareTo(new BigDecimal(params.get("total_amount"))) != 0) {
                return "failure";
            }

            //幂等:已支付直接 success
            if (PAY_SUCCESS.equals(pay.getPayStatus())) {
                return "success";
            }

            //保存回调原文
            pay.setNotifyJson(JSON.toJSONString(params));
            orderPayMapper.updateById(pay);

            if ("TRADE_SUCCESS".equals(tradeStatus) || "TRADE_FINISHED".equals(tradeStatus)) {
                markPaid(pay, params.get("trade_no"), parseDate(params.get("gmt_payment")));
            } else if ("TRADE_CLOSED".equals(tradeStatus)) {
                markClosed(pay);
            }
            return "success";
        } catch (Exception e) {
            log.error("支付宝异步通知处理失败", e);
            return "failure";
        }
    }

    @Override
    public void handleAlipayReturn(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //同步回跳仅作展示,不信任参数改库;真实状态以 notify/query 为准
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(
                "<!DOCTYPE html><html lang='zh'><head><meta charset='UTF-8'><title>支付结果</title></head>"
                        + "<body style='text-align:center;padding-top:80px;font-family:sans-serif'>"
                        + "<h2>支付流程已结束</h2>"
                        + "<p>请返回订单列表查看最新支付状态。</p>"
                        + "<button onclick='window.close()'>关闭本页</button>"
                        + "</body></html>");
    }

    /**
     * 生成商户订单号:前缀 + 订单id(采购 P + Integer,销售 S + 雪花String)
     */
    private String buildOutTradeNo(String bizType, String orderId) {
        return (BIZ_TYPE_PURCHASE.equals(bizType) ? "P" : "S") + orderId;
    }

    /**
     * 按业务类型+业务单号查询流水(每订单一行)
     */
    private OrderPay selectByBiz(String bizType, String orderId) {
        return orderPayMapper.selectOne(new LambdaQueryWrapper<OrderPay>()
                .eq(OrderPay::getBizType, bizType)
                .eq(OrderPay::getBizNo, orderId));
    }

    /**
     * 主动调用支付宝查询交易状态
     */
    private AlipayTradeQueryResponse queryAlipayTrade(String outTradeNo) {
        AlipayTradeQueryModel qm = new AlipayTradeQueryModel();
        qm.setOutTradeNo(outTradeNo);
        AlipayTradeQueryRequest qr = new AlipayTradeQueryRequest();
        qr.setBizModel(qm);
        try {
            return alipayClient.execute(qr);
        } catch (AlipayApiException e) {
            log.error("主动查询支付宝交易状态失败, outTradeNo={}", outTradeNo, e);
            return null;
        }
    }

    /**
     * 幂等置为已支付(带条件更新,并发安全)
     */
    private void markPaid(OrderPay pay, String tradeNo, Date payTime) {
        orderPayMapper.update(null, new LambdaUpdateWrapper<OrderPay>()
                .eq(OrderPay::getPayId, pay.getPayId())
                .eq(OrderPay::getPayStatus, PAY_WAIT)
                .set(OrderPay::getPayStatus, PAY_SUCCESS)
                .set(OrderPay::getTradeNo, tradeNo)
                .set(OrderPay::getPayTime, payTime));
        if (BIZ_TYPE_PURCHASE.equals(pay.getBizType())) {
            purchaseOrderService.update(new LambdaUpdateWrapper<PurchaseOrder>()
                    .eq(PurchaseOrder::getOrderId, Integer.valueOf(pay.getBizNo()))
                    .eq(PurchaseOrder::getPayStatus, PAY_WAIT)
                    .set(PurchaseOrder::getPayStatus, PAY_SUCCESS)
                    .set(PurchaseOrder::getPayTime, payTime));
        } else if (BIZ_TYPE_SALE.equals(pay.getBizType())) {
            saleOrderService.update(new LambdaUpdateWrapper<SaleOrder>()
                    .eq(SaleOrder::getOrderId, pay.getBizNo())
                    .eq(SaleOrder::getPayStatus, PAY_WAIT)
                    .set(SaleOrder::getPayStatus, PAY_SUCCESS)
                    .set(SaleOrder::getPayTime, payTime));
        }
    }

    /**
     * 幂等置为已关闭(带条件更新,并发安全)
     */
    private void markClosed(OrderPay pay) {
        orderPayMapper.update(null, new LambdaUpdateWrapper<OrderPay>()
                .eq(OrderPay::getPayId, pay.getPayId())
                .eq(OrderPay::getPayStatus, PAY_WAIT)
                .set(OrderPay::getPayStatus, PAY_CLOSED));
    }

    /**
     * 解析支付宝时间(yyyy-MM-dd HH:mm:ss),失败取当前时间
     */
    private Date parseDate(String s) {
        if (s == null || s.trim().isEmpty()) {
            return new Date();
        }
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
        } catch (Exception e) {
            return new Date();
        }
    }
}
