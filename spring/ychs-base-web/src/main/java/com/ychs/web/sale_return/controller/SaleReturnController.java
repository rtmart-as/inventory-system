/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_return.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.sale_return.entity.ApplyParm;
import com.ychs.web.sale_return.entity.SaleReturn;
import com.ychs.web.sale_return.entity.SaleReturnParm;
import com.ychs.web.sale_return.service.SaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RequestMapping("/api/saleReturn")
@RestController
public class SaleReturnController {
    @Autowired
    private SaleReturnService saleReturnService;

    //新增
    @PreAuthorize("hasAuthority('sys:sale:add')")
    @PostMapping
    public ResultVo add(@RequestBody SaleReturn saleReturn){
        saleReturn.setCreateTime(new Date());
        //调用save新增
        if(saleReturnService.save(saleReturn)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //删除
    @PreAuthorize("hasAuthority('sys:sale:delete')")
    @DeleteMapping("/{returnId}")
    public ResultVo delete(@PathVariable("returnId") Long returnId){
        //删除调用removeById
        saleReturnService.removeById(returnId);
        return ResultUtils.success("删除成功!");
    }

    //列表查询
    @GetMapping("/getList")
    public ResultVo getList(SaleReturnParm parm){
        IPage<SaleReturn> list = saleReturnService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }

    //审核
    @PostMapping("/apply")
    public ResultVo apply(@RequestBody ApplyParm parm){
        saleReturnService.applyOrder(parm);
        return ResultUtils.success("审批成功!");
    }
}
