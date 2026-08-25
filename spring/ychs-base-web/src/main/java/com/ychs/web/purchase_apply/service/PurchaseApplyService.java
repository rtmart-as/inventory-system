/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_apply.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ychs.web.purchase_apply.entity.ApplyParm;
import com.ychs.web.purchase_apply.entity.PurchaseApply;
import com.ychs.web.purchase_order.entity.StockApplyParm;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface PurchaseApplyService extends IService<PurchaseApply>   {
    //同意审核
    void doApplay(ApplyParm parm);
    //拒绝审核
    void returnApplay(ApplyParm parm);
    //减库存
    void subStock(StockApplyParm parm);
}
