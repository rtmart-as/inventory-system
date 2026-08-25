/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.sale_return.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ychs.web.sale_return.entity.SaleReturn;
import com.ychs.web.sale_return.entity.SaleReturnParm;
import org.apache.ibatis.annotations.Param;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public interface SaleReturnMapper extends BaseMapper<SaleReturn> {
    IPage<SaleReturn> getList(IPage<SaleReturn> page, @Param("parm") SaleReturnParm parm);
}
