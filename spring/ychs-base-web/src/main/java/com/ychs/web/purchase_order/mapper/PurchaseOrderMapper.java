/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.web.purchase_order.entity.PurchaseOrder;
import com.ychs.web.purchase_order.entity.PurchaseParm;
import com.ychs.web.purchase_order.entity.PurchaseTotal;
import com.ychs.web.purchase_order.entity.StockOrderParm;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {
    IPage<PurchaseOrder> getList(IPage<PurchaseOrder> page, @Param("supplierName") String supplierName, @Param(
            "goodsName") String goodsName);
    //减库存
    void subStock(@Param("parm") StockOrderParm parm);
    //采购每月统计
    List<PurchaseTotal> getPurchaseTotal(IPage<PurchaseTotal> page, @Param("parm") PurchaseParm parm);
}
