package com.ychs.web.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import com.ychs.web.consumer.service.ConsumerService;
import com.ychs.web.goods.service.GoodsService;
import com.ychs.web.home.entity.Echart;
import com.ychs.web.home.entity.EchartsItem;
import com.ychs.web.home.entity.TotalVo;
import com.ychs.web.purchase_apply.entity.PurchaseApply;
import com.ychs.web.purchase_apply.service.PurchaseApplyService;
import com.ychs.web.sale_order.entity.EcharTotalVo;
import com.ychs.web.sale_order.service.SaleOrderService;
import com.ychs.web.sys_notice.entity.SysNotice;
import com.ychs.web.sys_notice.service.SysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/home")
public class HomeController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private ConsumerService consumerService;
    @Autowired
    private PurchaseApplyService purchaseApplyService;

    @Autowired
    private SaleOrderService saleOrderService;
    @Autowired
    private SysNoticeService sysNoticeService;
    //首页销售排行
    @GetMapping("/getBestSale")
    public ResultVo getBestSale(){
        List<EchartsItem> hotGoods = saleOrderService.getBestSale();
        return ResultUtils.success("查询成功",hotGoods);
    }

    //首页热销商品
    @GetMapping("/getHotGoods")
    public ResultVo getHotGoods(){
        List<EchartsItem> hotGoods = saleOrderService.getHotGoods();
        return ResultUtils.success("查询成功",hotGoods);
    }

    //首页订单统计
    @GetMapping("/getEchartTotal")
    public ResultVo getEchartTotal(){
        List<EcharTotalVo> echartTotal = saleOrderService.getEchartTotal();
        //组装数据
        Echart echart = new Echart();
        List<String> names = new ArrayList<>();
        List<Integer> values = new ArrayList<>();
        if(echartTotal.size()>0){
            for (int i=0;i<echartTotal.size();i++){
                names.add(echartTotal.get(i).getOrderMonth());
                values.add(echartTotal.get(i).getTotalOrders());
            }
        }
        echart.setNames(names);
        echart.setValues(values);
        return ResultUtils.success("查询成功",echart);
    }



    //首页总数统计
    @GetMapping("/getTotal")
    public ResultVo getTotal(){
        TotalVo vo = new TotalVo();
        //商品总数
        long goodsCount = goodsService.count();
        vo.setGoodsCount(goodsCount);
        //库存预警
        int warnCount = goodsService.getWarnCount();
        vo.setStockCount(warnCount);
        //采购待审批
        QueryWrapper<PurchaseApply> apply = new QueryWrapper<>();
        apply.lambda().eq(PurchaseApply::getStatus,"0");
        int applyCount = purchaseApplyService.list(apply).size();
        vo.setWaitCount(applyCount);
        //客户总数
        long consumerCount = consumerService.count();
        vo.setConsumerCount(consumerCount);
        return ResultUtils.success("查询成功",vo);
    }
    
  /**
     * 首页公告
     * @return
     */
    @GetMapping("/getNoticeList")
    public ResultVo getNoticeList(){
        QueryWrapper<SysNotice> query = new QueryWrapper<>();
        query.lambda().orderByDesc(SysNotice::getCreateTime)
                .last(" limit 3");
        List<SysNotice> list = sysNoticeService.list(query);
        return ResultUtils.success("查询成功",list);
    }
}