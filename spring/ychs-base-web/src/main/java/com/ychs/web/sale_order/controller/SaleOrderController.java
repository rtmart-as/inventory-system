/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_order.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.purchase_order.entity.PurchaseParm;
import com.ychs.web.sale_order.entity.*;
import com.ychs.web.sale_order.service.SaleOrderService;
import com.ychs.web.sys_user.service.SysUserService;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/saleOrder")
public class SaleOrderController {
    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private SysUserService sysUserService;

    //新增
    @PreAuthorize("hasAuthority('sys:sale:add')")
    @PostMapping
    public ResultVo add(@RequestBody SaleOrder saleOrder){
        saleOrder.setCreateTime(new Date());
        //调用save新增
        if(saleOrderService.save(saleOrder)){
            return ResultUtils.success("新增成功!");
        }
        return ResultUtils.error("新增失败!");
    }

    //编辑
    @PreAuthorize("hasAuthority('sys:sale:edit')")
    @PutMapping
    public ResultVo edit(@RequestBody SaleOrder saleOrder){
        //编辑调用updateById
        if(saleOrderService.updateById(saleOrder)){
            return ResultUtils.success("编辑成功!");
        }
        return ResultUtils.error("编辑失败!");
    }

    //删除
    @PreAuthorize("hasAuthority('sys:sale:delete')")
    @DeleteMapping("/{orderId}")
    public ResultVo delete(@PathVariable("orderId") Long orderId){
        //删除调用removeById
        saleOrderService.removeById(orderId);
        return ResultUtils.success("删除成功!");
    }

    /**
     * 列表查询
     * @param parm
     * @return
     */
    @GetMapping("/getList")
    public ResultVo getList(SaleParm parm){
        IPage<SaleOrder> list = saleOrderService.getList(parm);
        return ResultUtils.success("查询成功",list);
    }

    //确定订单
    @PreAuthorize("hasAuthority('sys:sale:confirm')")
    @PostMapping("/confirmOrder")
    public ResultVo confirmOrder(@RequestBody SaleOrder saleOrder){
        saleOrderService.confirmOrder(saleOrder);
        return ResultUtils.success("新增成功!");
    }

    //每个商品每月报表
    @GetMapping("/getSaleTotal")
    public ResultVo getSaleTotal(PurchaseParm parm){
        IPage<SaleOrderTotal> saleTotal = saleOrderService.getSaleTotal(parm);
        return ResultUtils.success("查询成功",saleTotal);
    }


    //导出商品
    @RequestMapping("/exportGoods")
    public void exportGoods(HttpServletResponse response, PurchaseParm parm) throws Exception {
        IPage<SaleOrderTotal> list = saleOrderService.getSaleTotal(parm);
        //组装excel需要的数据格式
        List<ExportGoods> exportList = new ArrayList<>();
        if (list.getRecords().size() > 0) {
            for (int i = 0; i < list.getRecords().size(); i++) {
                ExportGoods vo = new ExportGoods();
                BeanUtils.copyProperties(list.getRecords().get(i), vo);
                exportList.add(vo);
            }
        }
        //导出
        String fileName = "商品销售报表.xlsx";
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExportGoods.class, exportList);
        // 下载
        downloadExcel(fileName, workbook, response);
    }

    //每个员工每月销售报表
    @GetMapping("/getEverySaleTotal")
    public ResultVo getEverySaleTotal(EveryParm parm){
        IPage<UserSaleTotal> everySaleTotal = saleOrderService.getEverySaleTotal(parm);
        return ResultUtils.success("查询成功",everySaleTotal);
    }


    //导出业绩
    @RequestMapping("/exportStaff")
    public void exportStaff(HttpServletResponse response, EveryParm parm) throws Exception {
        IPage<UserSaleTotal> list = saleOrderService.getEverySaleTotal(parm);
        //组装excel需要的数据格式
        List<ExportStaff> exportList = new ArrayList<>();
        if (list.getRecords().size() > 0) {
            for (int i = 0; i < list.getRecords().size(); i++) {
                ExportStaff vo = new ExportStaff();
                BeanUtils.copyProperties(list.getRecords().get(i), vo);
                exportList.add(vo);
            }
        }
        //导出
        String fileName = "业绩报表.xlsx";
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExportStaff.class, exportList);
        // 下载表格
        downloadExcel(fileName, workbook, response);
    }

    /**
     * 下载表格
     * @param fileName
     * @param workbook
     * @param response
     * @throws Exception
     */
    public static void downloadExcel(String fileName, Workbook workbook, HttpServletResponse response) throws Exception {
        try {
            if (StringUtils.isEmpty(fileName)) {
                throw new RuntimeException("导出文件名不能为空");
            }
            String encodeFileName = URLEncoder.encode(fileName, "UTF-8");
            response.setHeader("content-Type", "application/vnd.ms-excel; charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + encodeFileName);
            response.setHeader("FileName", encodeFileName);
            response.setHeader("Access-Control-Expose-Headers", "FileName");
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) {
            workbook.close();
        }
    }
}
