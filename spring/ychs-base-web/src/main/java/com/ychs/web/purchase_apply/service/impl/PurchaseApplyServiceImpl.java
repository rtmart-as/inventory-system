/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_apply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.status.StatusCode;
import com.ychs.web.purchase_apply.entity.ApplyParm;
import com.ychs.web.purchase_apply.entity.PurchaseApply;
import com.ychs.web.purchase_apply.mapper.PurchaseApplyMapper;
import com.ychs.web.purchase_apply.service.PurchaseApplyService;
import com.ychs.web.purchase_order.entity.PurchaseOrder;
import com.ychs.web.purchase_order.entity.StockApplyParm;
import com.ychs.web.purchase_order.service.PurchaseOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Service
public class PurchaseApplyServiceImpl extends ServiceImpl<PurchaseApplyMapper, PurchaseApply> implements PurchaseApplyService {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    @Override
    @Transactional
    public void doApplay(ApplyParm parm) {
        //查询采购信息
        PurchaseApply purchaseApply = this.baseMapper.selectById(parm.getApplyId());
        //更新申请单的状态
        PurchaseApply apply = new PurchaseApply();
        apply.setApplyId(parm.getApplyId());
        apply.setStatus(StatusCode.APPLAY_DO);
        apply.setApplyDesc(parm.getReason());
        apply.setApproveUser(parm.getApproveUser());
        this.baseMapper.updateById(apply);
        //生成采购订单
        PurchaseOrder order = new PurchaseOrder();
        order.setApplyId(parm.getApplyId());
        order.setGoodsId(purchaseApply.getGoodsId());
        order.setSupplierId(purchaseApply.getSupplierId());
        order.setPurchaseCount(purchaseApply.getPurchaseCount());
        order.setPurchasePrice(purchaseApply.getPurchasePrice());
        order.setStatus(StatusCode.ORDER_INTO_NO);
        order.setCreateTime(new Date());
        purchaseOrderService.save(order);
    }

    @Override
    public void returnApplay(ApplyParm parm) {
        PurchaseApply apply = new PurchaseApply();
        apply.setApplyId(parm.getApplyId());
        apply.setStatus(StatusCode.APPLAY_RETURN);
        apply.setApplyDesc(parm.getReason());
        apply.setApproveUser(parm.getApproveUser());
        this.baseMapper.updateById(apply);
    }

    @Override
    public void subStock(StockApplyParm parm) {
        this.baseMapper.subStock(parm);
    }
}
