/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.supplier.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@Data
@TableName("supplier")
public class Supplier implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 供应商id
     */
    @TableId(value = "supplier_id", type = IdType.AUTO)
    private Integer supplierId;

    /**
     * 供应商名称
     */
    private String supplierName;

    /**
     * 供应商地址
     */
    private String address;

    /**
     * 联系人
     */
    private String linkUser;

    /**
     * 联系电话
     */
    private String linkPhone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 邮编
     */
    private String postalCode;

    /**
     * 状态： 0 启用 1 停用
     */
    private String status;

    /**
     * 创建时间
     */
    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;
}
