import http from "@/http/index.ts";
//创建支付参数
export type PayParm = {
    bizType:string,
    orderId:string,
    activeQuery?:boolean
}
//创建支付返回
export type PayCreateVo = {
    url:string
}
//创建支付(电脑网站支付),返回收银台URL
export const createPayApi = (parm:PayParm)=>{
    return http.post("/api/pay/create",parm)
}
//查询支付状态
export const queryPayApi = (parm:PayParm)=>{
    return http.get("/api/pay/query",parm)
}
