/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.supplier.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.supplier.entity.SelectVo;
import com.ychs.web.supplier.entity.Supplier;
import com.ychs.web.supplier.entity.SupplierParm;
import com.ychs.web.supplier.service.SupplierService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/supplier")
public class SupplierController {
    @Autowired
    private SupplierService supplierService;

    // 新增
    @PreAuthorize("hasAuthority('sys:supplier:add')")
    @PostMapping
    public ResultVo add(@RequestBody Supplier supplier) {
        supplier.setCreateTime(new Date());
        // 调用serve新增
        if (supplierService.save(supplier)) {
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    // 编辑
    @PreAuthorize("hasAuthority('sys:supplier:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody Supplier supplier){
        //编辑调用updateById
        if(supplierService.updateById(supplier)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    // 删除
    @PreAuthorize("hasAuthority('sys:supplier:delete')")
    @DeleteMapping("/{supplierId}")
    public ResultVo delete(@PathVariable("supplierId") Long supplierId){
        //删除调用removeById
        supplierService.removeById(supplierId);
        return ResultUtils.success("删除成功!");
    }

    // 列表
    @GetMapping("/getList")
    public ResultVo getList(SupplierParm parm) {
        // 构造分页对象
        IPage<Supplier> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        // 构造查询条件
        QueryWrapper<Supplier> query = new QueryWrapper<>();
        query.lambda()
                .like(StringUtils.isNotEmpty(parm.getSupplierName()), Supplier::getSupplierName, parm.getSupplierName())
                .like(StringUtils.isNotEmpty(parm.getLinkUser()), Supplier::getLinkUser, parm.getLinkUser())
                .like(StringUtils.isNotEmpty(parm.getLinkPhone()), Supplier::getLinkPhone, parm.getLinkPhone());
        // 按创建时间倒叙
        query.lambda().orderByDesc(Supplier::getCreateTime);
        // 执行分页
        IPage<Supplier> list = supplierService.page(page, query);
        return ResultUtils.success("查询成功", list);
    }

    //供应商下拉数据
    @GetMapping("/selectList")
    public ResultVo selectList(){
        List<Supplier> list = supplierService.list();
        //返回的值
        List<SelectVo> selectItems = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item ->{
                    SelectVo vo = new SelectVo();
                    vo.setLabel(item.getSupplierName());
                    vo.setValue(item.getSupplierId());
                    selectItems.add(vo);
                });
        return  ResultUtils.success("查询成功",selectItems);
    }
}
