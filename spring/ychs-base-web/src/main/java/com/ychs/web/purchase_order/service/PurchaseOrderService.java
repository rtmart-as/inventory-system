/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_order.entity.EnterParm;
import com.ychs.web.purchase_order.entity.PurchaseOrder;
import com.ychs.web.purchase_order.entity.PurchaseParm;
import com.ychs.web.purchase_order.entity.PurchaseTotal;
import com.ychs.web.purchase_returning.entity.ReturnParm;

import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    IPage<PurchaseOrder> getList(PurchaseApplyParm parm);
    //验货入库
    void enterStock(EnterParm parm);
    //退货
    void returnStock(ReturnParm parm);
    //采购每月统计
    List<PurchaseTotal> getPurchaseTotal(PurchaseParm parm);
}
