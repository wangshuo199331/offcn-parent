package com.offcn.template;

import com.offcn.util.HttpUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;
@Component
@Slf4j
public class SmsTemplate {
    @Value("${sms.host}")
    private String host;
    @Value("${sms.path}")
    private String path;
    @Value("${sms.method}")
    private String method;
    @Value("${sms.appcode}")
    private String appcode;
    /*
     * querys 是作为传递参数的map集合
     * 包含 mobile 手机
     * param 验证码参数
     * tpl_id 模板id
     * */
    public String sendCode(Map<String,String> querys){
        Map<String, String> headers = new HashMap<String, String>();
//最后在header中的格式(中间是英文空格)为Authorization:APPCODE83359fd73fe94948385f570e3c139105
        headers.put("Authorization", "APPCODE " + appcode);
        Map<String, String> bodys = new HashMap<String, String>();
        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys,
                    bodys);
            String str = EntityUtils.toString(response.getEntity());
            System.out.println(str);
//发送后 返回结果字符串
            return str;
        } catch (Exception e) {
            e.printStackTrace();
//如果出现异常 返回fail
            return "fail";
        }
    }
}