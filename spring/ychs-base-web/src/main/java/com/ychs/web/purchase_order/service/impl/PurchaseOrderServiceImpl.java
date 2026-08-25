/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.status.StatusCode;
import com.ychs.web.goods.entity.StockParm;
import com.ychs.web.goods.service.GoodsService;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_apply.service.PurchaseApplyService;
import com.ychs.web.purchase_order.entity.*;
import com.ychs.web.purchase_order.mapper.PurchaseOrderMapper;
import com.ychs.web.purchase_order.service.PurchaseOrderService;
import com.ychs.web.purchase_returning.entity.PurchaseReturning;
import com.ychs.web.purchase_returning.entity.ReturnParm;
import com.ychs.web.purchase_returning.service.PurchaseReturningService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Service
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private PurchaseReturningService purchaseReturningService;
    @Autowired
    private PurchaseApplyService purchaseApplyService;

    @Override
    public IPage<PurchaseOrder> getList(PurchaseApplyParm parm) {
        //构造分页对象
        IPage<PurchaseOrder> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        return this.baseMapper.getList(page, parm.getSupplierName(), parm.getGoodsName());
    }

    @Override
    @Transactional
    public void enterStock(EnterParm parm) {
        //1、查询采购单信息
        PurchaseOrder order = this.baseMapper.selectById(parm.getOrderId());
        //2、更新对应商品库存数量
        StockParm stockParm = new StockParm();
        stockParm.setGoodsId(order.getGoodsId());
        stockParm.setGoodsCount(order.getPurchaseCount());
        goodsService.addStock(stockParm);
        //3、采购订单更新批号、验货人、更新状态为已入库
        PurchaseOrder update = new PurchaseOrder();
        update.setOrderId(order.getOrderId());
        //设置为已入库
        update.setStatus(StatusCode.ORDER_INTO_HAS);
        update.setBatchNum(parm.getBatchNum());
        update.setPurchaseUser(parm.getPurchaseUser());
        //更新采购单信息
        this.baseMapper.updateById(update);
    }

    @Override
    @Transactional
    public void returnStock(ReturnParm parm) {
        //1、查询采购单信息
        PurchaseOrder order = this.baseMapper.selectById(parm.getOrderId());
        //2、生成退货单信息
        PurchaseReturning returning = new PurchaseReturning();
        returning.setBatchNum(parm.getBatchNum());
        returning.setCreateTime(new Date());
        returning.setCreateUser(parm.getCreateUser());
        returning.setGoodsId(order.getGoodsId());
        returning.setReturnCount(parm.getReturnCount());
        returning.setReturnPrice(parm.getReturnPrice());
        returning.setSupplierId(order.getSupplierId());
        purchaseReturningService.save(returning);
        //3、减少采购订单数量
        StockOrderParm stockOrderParm = new StockOrderParm();
        stockOrderParm.setOrderId(order.getOrderId());
        stockOrderParm.setGoodsCount(parm.getReturnCount());
        this.baseMapper.subStock(stockOrderParm);
        //4、减少采购申请单采购数量
        StockApplyParm stockApplyParm = new StockApplyParm();
        stockApplyParm.setApplyId(order.getApplyId());
        stockApplyParm.setGoodsCount(parm.getReturnCount());
        purchaseApplyService.subStock(stockApplyParm);
        //5、判断是否入库：如果已入库，需要减少商品库存
        if (order.getStatus().equals(StatusCode.ORDER_INTO_HAS)) {//已入库
            StockParm stockParm = new StockParm();
            stockParm.setGoodsId(order.getGoodsId());
            stockParm.setGoodsCount(parm.getReturnCount());
            goodsService.subStock(stockParm);
        }
    }

    @Override
    public List<PurchaseTotal> getPurchaseTotal(PurchaseParm parm) {
        //构造分页查询对象
        IPage<PurchaseTotal> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        return this.baseMapper.getPurchaseTotal(page,parm);
    }
}