import http from "@/http/index.ts";
import type {UnwrapNestedRefs} from "vue";
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    supplierName:string,
    goodsName:string,
    total:number
}

//验货入库参数
export type EnterParm = {
    orderId:string,
    batchNum:string,
    purchaseUser:string
}

//退货参数
export type ReturnParm = {
    orderId:string,
    returnPrice:string,
    returnCount:string,
    batchNum:string,
    createUser:string
}

//报表参数
export type ExListParm = {
    currentPage:number,
    pageSize:number,
    startTime:string,
    endTime:string,
    total:number
}

//采购订单列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/purchaseOrder/getList",parm)
}
//验货入库
export const enterStockApi = (parm:EnterParm)=>{
    return http.post("/api/purchaseOrder/enterStock",parm)
}
//退货
export const returnStockApi = (parm:ReturnParm)=>{
    return http.post("/api/purchaseOrder/returnStock",parm)
}
//采购报表
export const getPurchaseTotalApi = (parm:ExListParm)=>{
    return http.get("/api/purchaseOrder/getPurchaseTotal",parm)
}
