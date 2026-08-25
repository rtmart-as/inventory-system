/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.goods.entity.GoodsParm;
import com.ychs.web.goods.entity.StockParm;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface GoodsService extends IService<Goods> {
    IPage<Goods> getList(GoodsParm parm);
    //加库存
    void addStock(StockParm parm);
    //减库存
    void subStock(StockParm parm);
    //查询库存预警数量
    int getWarnCount();
}
