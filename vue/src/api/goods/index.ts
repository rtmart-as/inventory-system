import http from "@/http/index.ts";

//商品数据类型
export type Goods = {
    goodsId: string,
    goodsName: string,
    supplierName?: string,
    supplierId: string,
    place: string,
    goodsDesc: string,
    salePrice: string,
    stock: string,
    stockWarn: string,
    image: string,
    speci: string,
    status: string,
    orderNum: string,
    goodsCode: string
}
//商品列表查询参数
export type GoodsParm = {
    currentPage: number,
    pageSize: number,
    goodsName: string,
    supplierName: string,
    goodsCode: string,
    total: number,
}
//新增
export const addApi = (parm:Goods) => {
    return http.post("/api/goods",parm)
}
//查询供应商下拉数据
export const selectListApi = () => {
    return http.get("/api/supplier/selectList")
}
//查询商品列表
export const getListApi = (parm:GoodsParm) => {
    return http.get("/api/goods/getList",parm)
}
//编辑
export const editApi = (parm:Goods)=>{
    return http.put("/api/goods",parm)
}
//删除
export const deleteApi = (goodsId:string)=>{
    return http.delete(`/api/goods/${goodsId}`)
}
//根据供应商id查询商品
export const selectGoodsListApi = (supplierId:string) => {
    return http.get("/api/goods/selectList",{
        supplierId:supplierId
    })
}
