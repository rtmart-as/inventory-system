package com.ychs.web.home.entity;

import lombok.Data;

@Data
public class TotalVo {
    private long goodsCount;
    private long stockCount;
    private long waitCount;
    private long consumerCount;
}