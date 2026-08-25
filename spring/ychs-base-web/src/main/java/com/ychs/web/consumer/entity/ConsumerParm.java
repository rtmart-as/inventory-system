package com.ychs.web.consumer.entity;

import lombok.Data;

@Data
public class ConsumerParm {
    //当前第几页
    private Long currentPage;
    //没有查询的条数
    private Long pageSize;
    //客户名称
    private String consumerName;
    //联系人
    private String linkUser;
    //联系电话
    private String linkPhone;
}