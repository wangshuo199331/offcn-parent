package com.offcn.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.Data;

import java.io.InputStream;
import java.util.Date;

@Data
public class UploadFileTemplate {

    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;
    private String bucketDomain;

    public String upLoadImg(InputStream inputStream,String fileName) {

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        //设置上传的存储空间名称 和 保存的文件名称
        int index = fileName.indexOf(".");
        String ext = fileName.substring(index);
        String newFileName = new Date().getTime()+ext;
        PutObjectResult putObjectResult = ossClient.putObject(bucketName, newFileName,inputStream);
        //https://scw-kingofmine.oss-cn-beijing.aliyuncs.com/testZoro.jpg
        //ucketdomain +fileName
        // 关闭OSSClient。
        ossClient.shutdown();
        return bucketDomain+newFileName;
    }
}
