/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_order.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.purchase_order.entity.*;
import com.ychs.web.purchase_order.service.PurchaseOrderService;
import com.ychs.web.purchase_returning.entity.ReturnParm;
import com.ychs.web.supplier.entity.Supplier;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/purchaseOrder")
public class PurchaseOrderController {
    @Autowired
    private PurchaseOrderService purchaseOrderService;

    /**
     * 列表
     * @param parm
     * @return
     */
    @GetMapping("/getList")
    public ResultVo getList(PurchaseOrderList parm){
        //构造分页对象
        IPage<PurchaseOrder> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        //构造查询条件
        MPJLambdaWrapper<PurchaseOrder> query = new MPJLambdaWrapper<>();
        query.selectAll(PurchaseOrder.class)
                .select(Goods::getGoodsName)
                .select(Supplier::getSupplierName)
                .innerJoin(Goods.class, Goods::getGoodsId, PurchaseOrder::getGoodsId)
                .innerJoin(Supplier.class, Supplier::getSupplierId, PurchaseOrder::getSupplierId)
                .like(StringUtils.isNotEmpty(parm.getGoodsName()), Goods::getGoodsName, parm.getGoodsName())
                .like(StringUtils.isNotEmpty(parm.getSupplierName()), Supplier::getSupplierName, parm.getSupplierName())
                .orderByDesc(PurchaseOrder::getCreateTime);
        IPage<PurchaseOrder> list = purchaseOrderService.page(page, query);
        return ResultUtils.success("查询成功",list);
    }

    //验货入库
    @PostMapping("/enterStock")
    public ResultVo enterStock(@RequestBody EnterParm parm){
        purchaseOrderService.enterStock(parm);
        return ResultUtils.success("验货入库成功");
    }

    //退货
    @PostMapping("/returnStock")
    public ResultVo returnStock(@RequestBody ReturnParm parm){
        purchaseOrderService.returnStock(parm);
        return ResultUtils.success("退货成功");
    }

    //每月采购统计
    @GetMapping("/getPurchaseTotal")
    public ResultVo getPurchaseTotal(PurchaseParm parm){
        List<PurchaseTotal> purchaseTotal = purchaseOrderService.getPurchaseTotal(parm);
        return ResultUtils.success("查询成功",purchaseTotal);
    }

    //导出采购商品
    @RequestMapping("/exportOrder")
    public void exportOrder(HttpServletResponse response, PurchaseParm parm) throws Exception {
        List<PurchaseTotal> list = purchaseOrderService.getPurchaseTotal(parm);
        //组装excel需要的数据格式
        List<ExportOrder> exportList = new ArrayList<>();
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                ExportOrder vo = new ExportOrder();
                BeanUtils.copyProperties(list.get(i), vo);
                exportList.add(vo);
            }
        }
        //导出
        String fileName = "采购报表.xlsx";
        ExportParams exportParams = new ExportParams();
        exportParams.setType(ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(exportParams, ExportOrder.class, exportList);
        // 下载
        downloadExcel(fileName, workbook, response);
    }

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
