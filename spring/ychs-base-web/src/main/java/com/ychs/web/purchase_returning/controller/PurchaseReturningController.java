package com.ychs.web.purchase_returning.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_returning.entity.PurchaseReturning;
import com.ychs.web.purchase_returning.service.PurchaseReturningService;
import com.ychs.web.supplier.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/purchaseReturning")
public class PurchaseReturningController {
    @Autowired
    private PurchaseReturningService purchaseReturningService;

    /**
     * 列表查询
     * @param parm
     * @return
     */
    @GetMapping("/getList")
    public ResultVo getList(PurchaseApplyParm parm){
        //构造分页对象
        IPage<PurchaseReturning> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        MPJLambdaWrapper<PurchaseReturning> query = new MPJLambdaWrapper<>();
        query.selectAll(PurchaseReturning.class)
                .select(Goods::getGoodsName)
                .select(Supplier::getSupplierName)
                .innerJoin(Goods.class,Goods::getGoodsId,PurchaseReturning::getGoodsId)
                .innerJoin(Supplier.class,Supplier::getSupplierId,PurchaseReturning::getSupplierId)
                .like(StringUtils.isNotEmpty(parm.getSupplierName()),Supplier::getSupplierName,parm.getSupplierName())
                .like(StringUtils.isNotEmpty(parm.getGoodsName()),Goods::getGoodsName,parm.getGoodsName())
                .orderByDesc(PurchaseReturning::getCreateTime);
        IPage<PurchaseReturning> list = purchaseReturningService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
}