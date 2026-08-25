package com.ychs.web.consumer.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.consumer.entity.Consumer;
import com.ychs.web.consumer.entity.ConsumerParm;
import com.ychs.web.consumer.service.ConsumerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/api/consumer")
@RestController
public class ConsumerController {
    @Autowired
    private ConsumerService consumerService;

    //新增
    @PreAuthorize("hasAuthority('sys:consumer:add')")
    @PostMapping
    public ResultVo add(@RequestBody Consumer consumer){
        consumer.setCreateTime(new Date());
        //调用save新增
        if(consumerService.save(consumer)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @PreAuthorize("hasAuthority('sys:consumer:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody Consumer consumer){
        //编辑调用updateById
        if(consumerService.updateById(consumer)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @PreAuthorize("hasAuthority('sys:consumer:delete')")
    @DeleteMapping("/{consumerId}")
    public ResultVo delete(@PathVariable("consumerId") Long consumerId){
        //删除调用removeById
        consumerService.removeById(consumerId);
        return ResultUtils.success("删除成功!");
    }

    //列表
    @GetMapping("/getList")
    public ResultVo getList(ConsumerParm parm){
        //构造分页对象
        IPage<Consumer> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<Consumer> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getConsumerName()), Consumer::getConsumerName,parm.getConsumerName())
                .like(StringUtils.isNotEmpty(parm.getLinkUser()),Consumer::getLinkUser,parm.getLinkUser())
                .like(StringUtils.isNotEmpty(parm.getLinkPhone()),Consumer::getLinkPhone,parm.getLinkPhone());
        query.lambda().orderByDesc(Consumer::getCreateTime);
        //list分页查询，调用的是 page;不分页 调用的是 list
        IPage<Consumer> list = consumerService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }
}