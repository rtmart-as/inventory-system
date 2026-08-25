package com.ychs.web.consumer.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@TableName("consumer")
public class Consumer implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 客户id
     */
    @TableId(value = "consumer_id", type = IdType.AUTO)
    private Integer consumerId;

    /**
     * 客户名称
     */
    private String consumerName;

    /**
     * 客户地址
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
     * 状态: 0 合作 1：停止合作
     */
    private String status;

    /**
     * 创建时间
     */
    //    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    @JsonFormat(pattern="yyyy-MM-dd",timezone="GMT+8")
    private Date createTime;

}
