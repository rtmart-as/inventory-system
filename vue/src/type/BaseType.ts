//弹框属性类型
export type DialogModel = {
    title: string,
    visible: boolean,
    height: number,
    width: number
}
//图片上传数据类型
export type NewType = {
    newImgUrl:Array<{url:string}>;
    deleteUrl:Array<{url:string}>;
}
