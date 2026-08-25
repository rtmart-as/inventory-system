/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_order.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.status.StatusCode;
import com.ychs.web.goods.entity.StockParm;
import com.ychs.web.goods.service.GoodsService;
import com.ychs.web.home.entity.EchartsItem;
import com.ychs.web.purchase_order.entity.PurchaseParm;
import com.ychs.web.sale_order.entity.*;
import com.ychs.web.sale_order.mapper.SaleOrderMapper;
import com.ychs.web.sale_order.service.SaleOrderService;
import com.ychs.web.sys_user.entity.SysUser;
import com.ychs.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Service
public class SaleOrderServiceImpl extends ServiceImpl<SaleOrderMapper, SaleOrder> implements SaleOrderService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SysUserService sysUserService;

    @Override
    public IPage<SaleOrder> getList(SaleParm parm) {
        //构造分页对象
        IPage<SaleOrder> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        return this.baseMapper.getList(page,parm);
    }

    @Override
    @Transactional
    public void confirmOrder(SaleOrder saleOrder) {
        SaleOrder order = this.baseMapper.selectById(saleOrder.getOrderId());
        //更新订单状态
        saleOrder.setStatus(StatusCode.SALE_ORDER_CONFIRM);
        this.baseMapper.updateById(saleOrder);
        //商品减库存
        StockParm stockParm = new StockParm();
        stockParm.setGoodsId(order.getGoodsId());
        stockParm.setGoodsCount(order.getSaleCount());
        goodsService.subStock(stockParm);
    }

    @Override
    public void subStock(SubOrderParm parm) {
        this.baseMapper.subStock(parm);
    }

    @Override
    public List<EcharTotalVo> getEchartTotal() {
        return this.baseMapper.getEchartTotal();
    }

    @Override
    public List<EchartsItem> getHotGoods() {
        return this.baseMapper.getHotGoods();
    }

    @Override
    public List<EchartsItem> getBestSale() {
        return this.baseMapper.getBestSale();
    }

    @Override
    public IPage<SaleOrderTotal> getSaleTotal(PurchaseParm parm) {
        IPage<SaleOrderTotal> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());

        return this.baseMapper.getSaleTotal(page,parm);
    }
    @Override
    public IPage<UserSaleTotal> getEverySaleTotal(EveryParm parm) {
        SysUser user = sysUserService.getById(parm.getUserId());
        IPage<UserSaleTotal> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //如果是超级管理员，查询全部
        if(StringUtils.isNotEmpty(user.getIsAdmin()) &&  user.getIsAdmin().equals("1")){
            parm.setUserId(null);
            return this.baseMapper.getEverySaleTotal(page,parm);
        }
        //员工只能根据自己id查询
        return this.baseMapper.getEverySaleTotal(page,parm);
    }
}
