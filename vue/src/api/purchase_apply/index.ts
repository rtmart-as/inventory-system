import http from "@/http/index.ts";
import type {UnwrapNestedRefs} from "vue";

//采购申请数据类型
export type PurchaseApply = {
    applyId: string,
    supplierId: string,
    goodsId: string,
    purchasePrice: string,
    purchaseCount: string,
    applyUser: string,
    approveUser: string,
    status?: string
}
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    supplierName:string,
    goodsName:string,
    total:number
}
//审批参数
export type ApplyParm = {
    applyId:string,
    type:string,
    reason:string,
    approveUser:string
}
//新增申请
export const addApi = (parm:PurchaseApply)=>{
    return http.post("/api/purchaseApply",parm)
}
//申请列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/purchaseApply/getList",parm)
}
//编辑
export const editApi =(parm:PurchaseApply)=>{
    return http.put("/api/purchaseApply",parm)
}
//删除
export const deleteApi =(applyId:string)=>{
    return http.delete(`/api/purchaseApply/${applyId}`)
}
//审批
export const doApplyApi =(parm:ApplyParm)=>{
    return http.post("/api/purchaseApply/doApply",parm)
}
