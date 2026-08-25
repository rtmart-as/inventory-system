import http from "@/http/index.ts";
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    supplierName:string,
    goodsName:string,
    total:number
}
//退货订单列表
export const getListApi = (parm:ListParm)=>{
    return http.get("/api/purchaseReturning/getList",parm)
}
