/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.web.home.entity.EchartsItem;
import com.ychs.web.purchase_order.entity.PurchaseParm;
import com.ychs.web.sale_order.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface SaleOrderMapper extends BaseMapper<SaleOrder> {
    IPage<SaleOrder> getList(IPage<SaleOrder> page, @Param("parm") SaleParm parm);
    //销售退货
    void subStock(@Param("parm")SubOrderParm parm);
    //首页订单统计
    List<EcharTotalVo> getEchartTotal();
    //首页热销商品
    List<EchartsItem> getHotGoods();
    //首页销售排行
    List<EchartsItem> getBestSale();
    //每月每个商品销售统计
    IPage<SaleOrderTotal> getSaleTotal(IPage<SaleOrderTotal> page, @Param("parm") PurchaseParm parm);
    //查询每个销售人员每月的销售金额
    IPage<UserSaleTotal> getEverySaleTotal(IPage<UserSaleTotal> page, @Param("parm") EveryParm parm);
}
