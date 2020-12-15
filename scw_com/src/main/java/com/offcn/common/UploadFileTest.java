package com.offcn.common;

import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class UploadFileTest {

    public static void main(String[] args) {
// Endpoint 使用自己的云服务存储地址
        String endpoint = "http://oss-cn-beijing.aliyuncs.com";
// 云账号AccessKey有所有API访问权限，建议遵循阿里云安全最佳实践，创建并使用RAM子账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建。
        String accessKeyId = "LTAI4GFqauxTzjdcmdBMAk4U";
        String accessKeySecret = "d0sgk3ggmKybM7ABv6m9sM5sX1cKqG";
// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
// 上传文件流。
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("D:\\img\\timg.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//设置上传的存储空间名称 和 保存的文件名称
        PutObjectResult putObjectResult = ossClient.putObject("scw-kingofmine", "testZoro.jpg",
                inputStream);
        System.out.println(putObjectResult.getResponse());
        System.out.println(JSON.toJSONString(putObjectResult));
        //https://scw-kingofmine.oss-cn-beijing.aliyuncs.com/testZoro.jpg
        //ucketdomain +fileName
// 关闭OSSClient。
        ossClient.shutdown();
    }
}
