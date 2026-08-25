/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.goods.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.web.goods.entity.Goods;
import com.ychs.web.goods.entity.GoodsParm;
import com.ychs.web.goods.entity.StockParm;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface GoodsMapper extends BaseMapper<Goods> {
    IPage<Goods> getList(IPage<Goods> page, @Param("parm") GoodsParm parm);
    //加库存
    void addStock(@Param("parm") StockParm parm);
    //减库存
    void subStock(@Param("parm") StockParm parm);
    //查询库存预警总数
    int getWarnCount();
}
