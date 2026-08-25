import http from "@/http/index.ts";
//数据类型
export type SysNotice = {
    noticeId: string,
    title: string,
    noticeText: string
}
//列表查询参数
export type SysNoticePageParm = {
    currentPage:number,
    pageSize:number,
    total:number,
    keywords:string
}
//新增
export const addApi = (parm:SysNotice) => {
    return http.post("/api/sysNotice",parm)
}
//编辑
export const editApi = (parm:SysNotice) => {
    return http.put("/api/sysNotice",parm)
}
//删除
export const deleteApi = (parm:SysNotice) => {
    return http.delete(`api/sysNotice/${parm.noticeId}`)
}
//查询
export const getListApi = (parm:SysNoticePageParm) => {
    return http.get("/api/sysNotice/getList",parm)
}
