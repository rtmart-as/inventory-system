/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_return.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ychs.web.sale_return.entity.ApplyParm;
import com.ychs.web.sale_return.entity.SaleReturn;
import com.ychs.web.sale_return.entity.SaleReturnParm;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface SaleReturnService extends IService<SaleReturn> {
    IPage<SaleReturn> getList(SaleReturnParm parm);
    //退货审批
    void applyOrder(ApplyParm parm);
}
