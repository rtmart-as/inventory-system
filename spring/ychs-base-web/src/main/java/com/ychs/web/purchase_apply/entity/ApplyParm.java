/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.purchase_apply.entity;

import lombok.Data;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
public class ApplyParm {
    private Integer applyId;
    //区分同意还是拒绝：0 ：同意 1：拒绝
    private String type;
    //原因备注
    private String reason;
    private String approveUser;
}