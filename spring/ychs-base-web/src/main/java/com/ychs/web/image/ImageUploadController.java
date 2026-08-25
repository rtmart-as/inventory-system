/*
 * 英才汇硕信息科技有限公司 拥有本软件版权 2024-2028 并保留所有权利。
 * Copyright 2024-2028, YCHS Information&Science Technology Co.,Ltd,
 * All rights reserved.
 */
package com.ychs.web.image;

import com.ychs.utils.ResultUtils;
import com.ychs.utils.ResultVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

/**
 *
 * @author fanyuyang
 * @version 1.0
 */
@RestController
@RequestMapping("/api/upload")
public class ImageUploadController {
    //获取图片上传路径
    @Value("${web.uploadpath}")
    private String webUploadpath;

    @RequestMapping("/uploadImage")
    public ResultVo uploadImage(@RequestParam("file") MultipartFile file){
        //上传成功后的图片路径，返回给前端
        String Url = "";
        //获取上传图片的名称
        String fileName = file.getOriginalFilename();
        // aa.png  获取图片的扩展名 png
        String fileExtenionName = fileName.substring(fileName.indexOf("."));
        //生成新的文件名
        String newName = UUID.randomUUID().toString() + fileExtenionName;
        String path = webUploadpath;
        File fileDir = new File(path);
        if(!fileDir.exists()){
            //创建
            fileDir.mkdirs();
            //设置权限
            fileDir.setWritable(true);
        }
        File targetFile = new File(path,newName);
        try{
            file.transferTo(targetFile);
            Url = "/"  + targetFile.getName();
        }catch (Exception e){
            return  null;
        }
        return ResultUtils.success("上传成功","/images" + Url);
    }
}
