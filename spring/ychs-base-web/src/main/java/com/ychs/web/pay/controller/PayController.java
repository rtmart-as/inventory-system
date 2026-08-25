package com.ychs.web.pay.controller;

import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.pay.service.PayService;
import com.ychs.web.pay.vo.PayCreateParm;
import com.ychs.web.pay.vo.PayCreateVo;
import com.ychs.web.pay.vo.PayQueryParm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 支付接口
 */
@RestController
@RequestMapping("/api/pay")
public class PayController {
    @Autowired
    private PayService payService;

    /**
     * 创建支付(电脑网站支付),返回收银台跳转URL
     */
    @PostMapping("/create")
    public ResultVo create(@RequestBody PayCreateParm parm) {
        try {
            PayCreateVo vo = payService.createPay(parm.getBizType(), parm.getOrderId());
            return ResultUtils.success("创建支付成功", vo);
        } catch (RuntimeException e) {
            return ResultUtils.error(e.getMessage());
        }
    }

    /**
     * 查询支付状态(默认查本地库)
     */
    @GetMapping("/query")
    public ResultVo query(PayQueryParm parm) {
        return ResultUtils.success("查询成功",
                payService.queryPay(parm.getBizType(), parm.getOrderId(),
                        Boolean.TRUE.equals(parm.getActiveQuery())));
    }
}
