import http from "@/http/index.ts";
//图片上传
export const uploadImageApi = (parm:any)=>{
    return http.upload("/api/upload/uploadImage",parm)
}

