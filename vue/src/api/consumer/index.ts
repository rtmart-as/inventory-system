import http from "@/http/index.ts";
//客户数据类型
export type Consumer = {
    consumerId: string,
    consumerName: string,
    address: string,
    linkUser: string,
    linkPhone: string,
    email: string,
    postalCode: string,
    status: string,
}
//列表查询参数
export type ListParm = {
    currentPage:number,
    pageSize:number,
    consumerName:string,
    linkUser:string,
    linkPhone:string,
    total:number
}
//新增
export const addApi = (parm:Consumer) => {
    return http.post("/api/consumer",parm)
}
//查询
export const getListApi = (parm:ListParm) => {
    return http.get("/api/consumer/getList",parm)
}
//编辑
export const editApi = (parm:Consumer) => {
    return http.put("/api/consumer",parm)
}
//删除
export const deleteApi = (parm:Consumer) => {
    return http.delete(`/api/consumer/${parm.consumerId}`)
}
