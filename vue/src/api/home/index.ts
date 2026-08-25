import http from "@/http/index.ts";
//查询总数
export const getTotalApi = () => {
    return http.get("/api/home/getTotal")
}
//首页销售排行
export const getBestSaleApi = () => {
    return http.get("/api/home/getBestSale")
}
//首页热销排行
export const getHotGoodsApi = () => {
    return http.get("/api/home/getHotGoods")
}
//首页订单统计
export const getEchartTotalApi = () => {
    return http.get("/api/home/getEchartTotal")
}
//首页公告
export const getNoticeListApi = () => {
    return http.get("/api/home/getNoticeList")
}
