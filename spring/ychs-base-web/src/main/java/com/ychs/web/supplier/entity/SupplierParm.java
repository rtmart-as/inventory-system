package com.ychs.web.supplier.entity;

import lombok.Data;

@Data
public class SupplierParm {
    // 当前第几页（前端 el-pagination 传 currentPage）
    private Long currentPage;
    // 每页条数（前端传 pageSize）
    private Long pageSize;
    // 供应商名称（可选模糊查询条件，不传就查全部）
    private String supplierName;
    // 联系人（可选模糊查询条件）
    private String linkUser;
    // 联系电话（可选模糊查询条件）
    private String linkPhone;
}