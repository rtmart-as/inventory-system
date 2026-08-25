package com.ychs.web.sale_order.entity;

import lombok.Data;

@Data
public class EveryParm {
    //当前第几页
    private Long currentPage;
    //每月查询的条数
    private Long pageSize;
    //开始时间
    private String startTime;
    //结束时间
    private String endTime;

    private String userId;
}