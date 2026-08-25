import http from "@/http";   // 项目封装的 axios 实例（自动带 baseURL / token 拦截）
// 供应商数据类型 —— 与后端 Supplier 实体字段一一对应，前端 TS 类型校验
export type Supplier = {
    supplierId: string,
    supplierName: string,
    address: string,
    linkUser: string,
    linkPhone: string,
    email: string,
    postalCode: string,
    status: string,
}
// 列表查询参数 —— 与后端 SupplierParm 对应
export type ListParm = {
    currentPage: number,
    pageSize: number,
    supplierName: string,
    linkUser: string,
    linkPhone: string,
    total: number,   // 后端返回的总条数，供分页组件显示
}
// 新增
export const addApi = (parm: Supplier) => http.post("/api/supplier", parm)
// 编辑
export const editApi = (parm: Supplier) => http.put("/api/supplier", parm)
// 删除 —— 用模板字符串把 id 拼进 URL：/api/supplier/5
export const deleteApi = (supplierId: string) => http.delete(`/api/supplier/${supplierId}`)
// 列表查询
export const getListApi = (parm: ListParm) => http.get("/api/supplier/getList", parm)
