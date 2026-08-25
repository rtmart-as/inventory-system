package com.ychs.web.sys_notice.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.sys_notice.entity.SysNotice;
import com.ychs.web.sys_notice.entity.SysNoticeParm;
import com.ychs.web.sys_notice.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RequestMapping("/api/sysNotice")
@RestController
public class SysNoticeController {
    @Autowired
    private SysNoticeService sysNoticeService;

    //新增
    @PreAuthorize("hasAuthority('sys:sysNotice:add')")
    @PostMapping
    public ResultVo add(@RequestBody SysNotice sysNotice){
        sysNotice.setCreateTime(new Date());
        //调用save新增
        if(sysNoticeService.save(sysNotice)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @PreAuthorize("hasAuthority('sys:sysNotice:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody SysNotice sysNotice){
        //编辑调用updateById
        if(sysNoticeService.updateById(sysNotice)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @PreAuthorize("hasAuthority('sys:sysNotice:delete')")
    @DeleteMapping("/{noticeId}")
    public ResultVo delete(@PathVariable("noticeId") Long noticeId){
        //删除调用removeById
        sysNoticeService.removeById(noticeId);
        return ResultUtils.success("删除成功!");
    }

    //列表
    @GetMapping("/getList")
    public ResultVo getList(SysNoticeParm parm){
        //构造分页对象
        IPage<SysNotice> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        QueryWrapper<SysNotice> query = new QueryWrapper<>();
        query.lambda().like(StringUtils.isNotEmpty(parm.getKeywords()),SysNotice::getTitle,parm.getKeywords())
                .or()
                .like(StringUtils.isNotEmpty(parm.getKeywords()),SysNotice::getNoticeText,parm.getKeywords())
       .orderByDesc(SysNotice::getCreateTime);
        //list分页查询，调用的是 page;不分页 调用的是 list
        IPage<SysNotice> list = sysNoticeService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

}