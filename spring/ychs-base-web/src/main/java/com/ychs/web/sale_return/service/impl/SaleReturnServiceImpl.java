package com.ychs.web.sale_return.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.status.StatusCode;
import com.ychs.web.goods.entity.StockParm;
import com.ychs.web.goods.service.GoodsService;
import com.ychs.web.sale_order.entity.SubOrderParm;
import com.ychs.web.sale_order.service.SaleOrderService;
import com.ychs.web.sale_return.entity.ApplyParm;
import com.ychs.web.sale_return.entity.SaleReturn;
import com.ychs.web.sale_return.entity.SaleReturnParm;
import com.ychs.web.sale_return.mapper.SaleReturnMapper;
import com.ychs.web.sale_return.service.SaleReturnService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SaleReturnServiceImpl extends ServiceImpl<SaleReturnMapper, SaleReturn> implements SaleReturnService {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SaleOrderService saleOrderService;

    @Override
    public IPage<SaleReturn> getList(SaleReturnParm parm) {
        //构造分页对象
        IPage<SaleReturn> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());
        return this.baseMapper.getList(page,parm);
    }

    //退货审批
    @Override
    @Transactional
    public void applyOrder(ApplyParm parm) {
        //查询退货单信息
        SaleReturn aReturn = this.baseMapper.selectById(parm.getReturnId());
        //更新退货单状态
        SaleReturn saleReturn = new SaleReturn();
        saleReturn.setReturnId(String.valueOf(parm.getReturnId()));
        saleReturn.setStatus(parm.getType());
        saleReturn.setApplyDesc(parm.getApplyDesc());
        this.baseMapper.updateById(saleReturn);
        if(parm.getType().equals(StatusCode.RETURN_APPLAY_DO)){//同意退货
            //销售订单减数量
            SubOrderParm subOrderParm = new SubOrderParm();
            subOrderParm.setOrderId(aReturn.getOrderId());
            subOrderParm.setSaleCount(aReturn.getReturnCount());
            subOrderParm.setSalePrice(aReturn.getReturnPrice());
            saleOrderService.subStock(subOrderParm);
            //商品库存加数量
            StockParm stockParm = new StockParm();
            stockParm.setGoodsId(aReturn.getGoodsId());
            stockParm.setGoodsCount(aReturn.getReturnCount());
            goodsService.addStock(stockParm);
        }
    }
}

