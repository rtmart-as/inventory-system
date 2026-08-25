package com.ychs.web.purchase_returning.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.web.purchase_apply.entity.PurchaseApplyParm;
import com.ychs.web.purchase_returning.entity.PurchaseReturning;
import com.ychs.web.purchase_returning.mapper.PurchaseReturningMapper;
import com.ychs.web.purchase_returning.service.PurchaseReturningService;
import org.springframework.stereotype.Service;

@Service
public class PurchaseReturningServiceImpl extends ServiceImpl<PurchaseReturningMapper, PurchaseReturning> implements PurchaseReturningService {
    @Override
    public IPage<PurchaseReturning> getList(PurchaseApplyParm parm) {
        //构造分页对象
        IPage<PurchaseReturning> page = new Page<>(parm.getCurrentPage(), parm.getPageSize());
        return this.baseMapper.getList(page, parm.getSupplierName(), parm.getGoodsName());
    }
}