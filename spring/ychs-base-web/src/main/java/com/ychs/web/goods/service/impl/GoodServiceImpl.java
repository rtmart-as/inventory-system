/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.goods.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.goods.entity.GoodsParm;
import com.ychs.web.goods.entity.StockParm;
import com.ychs.web.goods.mapper.GoodsMapper;
import com.ychs.web.goods.service.GoodsService;
import org.springframework.stereotype.Service;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Service
public class GoodServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Override
    public IPage<Goods> getList(GoodsParm parm) {
        IPage<Goods> page = new Page<>(parm.getCurrentPage(),parm.getPageSize());

        return this.baseMapper.getList(page,parm);
    }

    @Override
    public void addStock(StockParm parm) {
        this.baseMapper.addStock(parm);
    }

    @Override
    public void subStock(StockParm parm) {
        this.baseMapper.subStock(parm);
    }

    @Override
    public int getWarnCount() {
        return this.baseMapper.getWarnCount();
    }
}
