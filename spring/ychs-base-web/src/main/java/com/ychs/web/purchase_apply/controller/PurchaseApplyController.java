/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_apply.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ychs.status.StatusCode;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.purchase_apply.entity.ApplyParm;
import com.ychs.web.purchase_apply.entity.PurchaseApply;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_apply.service.PurchaseApplyService;
import com.ychs.web.supplier.entity.Supplier;
import com.ychs.web.sys_user.entity.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/purchaseApply")
public class PurchaseApplyController {
    @Autowired
    private PurchaseApplyService purchaseApplyService;

    // 新增
    @PreAuthorize("hasAuthority('sys:purchaseApply:add')")
    @PostMapping
    public ResultVo add(@RequestBody PurchaseApply purchaseApply){
        purchaseApply.setApplyTime(new Date());
        purchaseApply.setStatus("0");
        //调用save新增
        if(purchaseApplyService.save(purchaseApply)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    // 编辑
    @PreAuthorize("hasAuthority('sys:purchaseApply:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody PurchaseApply purchaseApply){
        //编辑调用updateById
        if(purchaseApplyService.updateById(purchaseApply)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    // 删除
    @PreAuthorize("hasAuthority('sys:purchaseApply:delete')")
    @DeleteMapping("/{applyId}")
    public ResultVo delete(@PathVariable("applyId") Long applyId){
        //删除调用removeById
        purchaseApplyService.removeById(applyId);
        return ResultUtils.success("删除成功!");
    }

    @GetMapping("/getList")
    public ResultVo getList(PurchaseApplyParm parm){
        //构造分页对象
        IPage<PurchaseApply> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        MPJLambdaWrapper<PurchaseApply> query = new MPJLambdaWrapper<>();
        query.selectAll(PurchaseApply.class)
                .select(Goods::getGoodsName)
                .select(Supplier::getSupplierName)
                .select(SysUser::getNickName)
                .innerJoin(Goods.class,Goods::getGoodsId,PurchaseApply::getGoodsId)
                .innerJoin(Supplier.class,Supplier::getSupplierId,PurchaseApply::getSupplierId)
                .innerJoin(SysUser.class,SysUser::getUserId,PurchaseApply::getApplyUser)
                .like(StringUtils.isNotEmpty(parm.getGoodsName()),Goods::getGoodsName,parm.getGoodsName())
                .like(StringUtils.isNotEmpty(parm.getSupplierName()),Supplier::getSupplierName,parm.getSupplierName())
                .orderByDesc(PurchaseApply::getApplyTime);
        //list分页查询，调用的是 page;不分页 调用的是 list
        IPage<PurchaseApply> list = purchaseApplyService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    @PreAuthorize("hasAuthority('sys:purchaseApply:apply')")
    @PostMapping("/doApply")
    public ResultVo doApply(@RequestBody ApplyParm parm){
        //查看是否已经审核
        PurchaseApply apply = purchaseApplyService.getById(parm.getApplyId());
        if(apply.getStatus().equals(StatusCode.APPLAY_DO)){
            return ResultUtils.error("申请单已经审批，不要重复审批!");
        }
        if(parm.getType().equals("0")){//同意审批
            purchaseApplyService.doApplay(parm);
            return ResultUtils.success("审批成功!");
        }else{
            purchaseApplyService.returnApplay(parm);
            return ResultUtils.success("拒绝成功!");
        }
    }
}
