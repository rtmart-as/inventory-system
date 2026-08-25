import http from "@/http/index.ts";
//销售单数据类型
export type SaleReturn = {
    orderId: string,
    goodsId: string,
    goodsName: string,
    returnPrice: string,
    returnCount: string,
    createUser: string,
    description: string,
    status: string
}
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    supplierName:string,
    goodsName:string,
    createUser:string,
    nickName:string,
    total:number
}
//审核参数
export type ApplyParm = {
    returnId: string,
    type: string,
    applyDesc: string,
    applyUser: string
}
//新增
export const applyReturnApi = (parm:SaleReturn)=>{
    return http.post("/api/saleReturn",parm)
}
//列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/saleReturn/getList",parm)
}
//审核
export const applyApi = (parm:ApplyParm)=>{
    return http.post("/api/saleReturn/apply",parm)
}
//删除
export const deleteApi = (returnId:string)=>{
    return http.delete(`/api/saleReturn/${returnId}`)
}
