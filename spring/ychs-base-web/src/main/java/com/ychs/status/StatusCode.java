/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.status;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
public class StatusCode {
    //返回成功
    public static final int SUCCESS_CODE = 200;
    //错误状态码
    public static final int ERROR_CODE = 500;
    //无权限
    public static final int NO_LOGIN = 600;
    public static final int NO_AUTH = 700;
    //采购单待拒绝状态 状态：0：待审批 1：已审批 2：拒绝
    public static final String APPLAY_WAIT = "0";
    public static final String APPLAY_DO = "1";
    public static final String APPLAY_RETURN = "2";
    //采购单入库状态: 0未入库
    public static final String ORDER_INTO_NO = "0";
    //采购单入库状态: 1已入库
    public static final String ORDER_INTO_HAS = "1";
    //销售订单确定: 1已确定
    public static final String SALE_ORDER_CONFIRM = "1";
    public static final String RETURN_APPLAY_DO = "1";
}