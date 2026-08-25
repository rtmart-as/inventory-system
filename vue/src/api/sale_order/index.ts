import http from "@/http/index.ts";
//销售单数据类型
export type SaleOrder = {
    orderId:string,
    goodsId:string,
    goodsName:string,
    supplierId:string,
    salePrice:string,
    saleCount:string,
    status:string,
    payStatus?:string,
    saleUser:string
}
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    supplierName:string,
    goodsName:string,
    nickName:string,
    total:number
}
export type TotalListParm = {
    currentPage: number,
    pageSize: number,
    startTime: string,
    endTime: string,
    total: number,
}
//新增
export const addApi = (parm:SaleOrder)=>{
    return http.post("/api/saleOrder",parm)
}
//列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/saleOrder/getList",parm)
}
//编辑
export const editApi = (parm:SaleOrder)=>{
    return http.put("/api/saleOrder",parm)
}
//删除
export const deleteApi = (orderId:string)=>{
    return http.delete(`/api/saleOrder/${orderId}`)
}
//确认订单
export const confirmOrderApi = (parm:SaleOrder)=>{
    return http.post("/api/saleOrder/confirmOrder",parm)
}
//每个商品，每个月销售报表
export const getSaleTotalApi = (parm: TotalListParm) => {
    return http.get("/api/saleOrder/getSaleTotal", parm)
}
//每个员工每月销售报表
export const getEverySaleTotalApi = (parm: TotalListParm) => {
    return http.get("/api/saleOrder/getEverySaleTotal", parm)
}
