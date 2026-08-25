/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.goods.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.goods.entity.GoodsParm;
import com.ychs.web.goods.service.GoodsService;
import com.ychs.web.supplier.entity.SelectVo;
import com.ychs.web.supplier.entity.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsController {
    @Autowired
    private GoodsService goodsService;

    //图片上传路径
    @Value("${web.uploadpath}")
    private String webUploadpath;

    // 新增
    @PreAuthorize("hasAuthority('sys:goods:add')")
    @PostMapping
    public ResultVo add(@RequestBody Goods goods){
        //调用save新增
        if(goodsService.save(goods)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    // 编辑
    @PreAuthorize("hasAuthority('sys:goods:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody Goods goods){
        //编辑调用updateById
        if(goodsService.updateById(goods)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    // 删除
    @PreAuthorize("hasAuthority('sys:goods:delete')")
    @DeleteMapping("/{goodsId}")
    public ResultVo delete(@PathVariable("goodsId") Long goodsId){
        //删除数据库记录前，先查图片并删除本地文件，避免磁盘残留
        Goods goods = goodsService.getById(goodsId);
        if(goods != null && StringUtils.isNotEmpty(goods.getImage())){
            // image 形如 /images/xxx.png，取最后一段作为文件名
            String fileName = goods.getImage().substring(goods.getImage().lastIndexOf("/") + 1);
            File file = new File(webUploadpath, fileName);
            if(file.exists()){
                file.delete();
            }
        }
        //删除调用removeById
        goodsService.removeById(goodsId);
        return ResultUtils.success("删除成功!");
    }

    // 列表
    @GetMapping("/getList")
    public ResultVo getList(GoodsParm parm){
        //构造分页对象
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        MPJLambdaWrapper<Goods> query = new MPJLambdaWrapper<>();
        query.selectAll(Goods.class) //商品表的全部字段
                .select(Supplier::getSupplierName) //查供应商的名称
                .leftJoin(Supplier.class, Supplier::getSupplierId,Goods::getSupplierId)
                .like(StringUtils.isNotEmpty(parm.getSupplierName()),Supplier::getSupplierName,parm.getSupplierName())
                .like(StringUtils.isNotEmpty(parm.getGoodsName()),Goods::getGoodsName,parm.getGoodsName())
                .like(StringUtils.isNotEmpty(parm.getGoodsCode()),Goods::getGoodsCode,parm.getGoodsCode());
        query.orderByDesc(Goods::getOrderNum);
        //list分页查询，调用的是 page;不分页 调用的是 list
        IPage<Goods> list = goodsService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //根据供应商id查询商品列表（采购申请弹窗下拉用）
    @GetMapping("/selectList")
    public ResultVo selectList(Integer supplierId){
        QueryWrapper<Goods> query = new QueryWrapper<>();
        query.lambda().eq(Goods::getSupplierId,supplierId);
        List<Goods> list = goodsService.list(query);
        //返回的值
        List<SelectVo> selectItmes = new ArrayList<>();
        Optional.ofNullable(list).orElse(new ArrayList<>())
                .forEach(item ->{
                    SelectVo vo = new SelectVo();
                    vo.setLabel(item.getGoodsName());
                    vo.setValue(item.getGoodsId());
                    selectItmes.add(vo);
                });
        return  ResultUtils.success("查询成功",selectItmes);
    }
}
