/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_returning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.web.purchase_returning.entity.PurchaseReturning;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface PurchaseReturningMapper extends BaseMapper<PurchaseReturning> {
    IPage<PurchaseReturning> getList(IPage<PurchaseReturning> page, @Param("supplierName") String supplierName, @Param("goodsName") String goodsName);
}
