package com.ychs.web.sys_notice.entity;

import lombok.Data;

@Data
public class SysNoticeParm {
    //当前第几页
    private Long currentPage;
    //没有查询的条数
    private Long pageSize;
    //查询关键字
    private String keywords;
}