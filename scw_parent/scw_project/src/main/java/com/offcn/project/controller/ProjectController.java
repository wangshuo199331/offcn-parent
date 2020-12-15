package com.offcn.project.controller;


import com.offcn.common.AppResponse;
import com.offcn.util.UploadFileTemplate;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("project")
public class ProjectController {

    @Autowired
    private UploadFileTemplate uploadFileTemplate;

    @PostMapping("uploadProjectImg")
    @ApiOperation(value="用于实现文件上传")
    public AppResponse uploadProjectImg(MultipartFile file){


        if (file!=null){
            String fileName = file.getOriginalFilename(); 
            try {
                InputStream inputStream = file.getInputStream();
                String url = uploadFileTemplate.upLoadImg(inputStream, fileName);
                return AppResponse.ok(url);
            } catch (IOException e) {
                e.printStackTrace();
                return AppResponse.ivalidParam("服务器错误");
            }
        }
        return AppResponse.ivalidParam("文件数据有误");
    }
}
