/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_returning.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_returning.entity.PurchaseReturning;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface PurchaseReturningService extends IService<PurchaseReturning>  {
    IPage<PurchaseReturning> getList(PurchaseApplyParm parm);
}
