package com.offcn.user.controller;

import com.offcn.common.AppResponse;
import com.offcn.pojo.RegisterVo;
import com.offcn.template.SmsTemplate;
import com.offcn.user.pojo.TMember;
import com.offcn.user.pojo.TMemberAddress;
import com.offcn.user.service.UserService;
import com.offcn.util.PhoneFormatCheckUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("user")
    @RestController
    @Api(tags="整个用户操作的接口类")

public class UserController {

    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private SmsTemplate smsTemplate;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserService userService;

    @PostMapping("addUser")
    @ApiOperation(value="用户注册请求的接口")
    //@ApiImplicitParam(value = "registerVo",required = true)
    public AppResponse addUser(@RequestBody RegisterVo registerVo){
        String code = stringRedisTemplate.boundValueOps(registerVo.getLoginacct()).get();
        if (!registerVo.getCode().equals(code)){
            return AppResponse.fail(null);
        }
        TMember member = new TMember();
        BeanUtils.copyProperties(registerVo,member);
        try {
            userService.addUser(member);
            return AppResponse.ok(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AppResponse.fail(null);
    }
    @GetMapping("sendCode")
    @ApiOperation(value="发送验证码到手机")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户手机号",name = "phoneNum",paramType = "query"),
    })
    public AppResponse sendCode(String phoneNum){

        boolean b = PhoneFormatCheckUtils.isChinaPhoneLegal(phoneNum);
        if (b){
            Map map = new HashMap<>();
            map.put("mobile",phoneNum);
            Integer code =new Random().nextInt(100000)+899999;
            map.put("param","code:"+code);
            map.put("tpl_id","TP1711063");
            String resultStr = smsTemplate.sendCode(map);
            if (resultStr.equals("")||resultStr.equals("fail")){
                return AppResponse.fail(null);
            }
            stringRedisTemplate.boundValueOps(phoneNum).set(code+"");
            return AppResponse.ok(null);
        }else {
            return AppResponse.ivalidParam(null);
        }
    }
    @GetMapping("userLogin")
    @ApiOperation(value="根据用户名密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(value ="用户手机号",name = "logincact",paramType = "query"),
            @ApiImplicitParam(value ="用户密码",name = "pwd",paramType = "query"),
    })
    public AppResponse sendCode(String logincact,String pwd){
        TMember member = userService.findByNameAndPwd(logincact,pwd);
        if(member!=null){
            //返回的是 用户的唯一凭证 token
            String token = UUID.randomUUID().toString().replace("-","");
            //生成的令牌和用户id存储到缓存中
            stringRedisTemplate.boundValueOps(token).set(member.getId()+"");
            System.out.println("token-----"+token);
            return AppResponse.ok(token);
        }else{
            return AppResponse.fail("用户不存在");
        }
    }
    @GetMapping("findById")
    @ApiOperation(value="根据用户token查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户令牌",name = "token",paramType = "query"),
    })
    public AppResponse findById(String token){
        String userId= stringRedisTemplate.boundValueOps(token).get();
        if (StringUtils.isEmpty(userId)){
            return AppResponse.ivalidParam("用户token无效");
        }
        TMember member = userService.findByUserId(userId);
        if (member!=null){
            return AppResponse.ok(member);
        }else{
            return AppResponse.fail("用户不存在");
        }
    }
    @GetMapping("findAddressList")
    @ApiOperation(value="根据用户token查询")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户令牌",name = "token",paramType = "query"),
    })
    public AppResponse findAddressList(String token){
        String userId= stringRedisTemplate.boundValueOps(token).get();
        if (StringUtils.isEmpty(userId)){
            return AppResponse.ivalidParam("用户token无效");
        }
        List<TMemberAddress> tMemberAddresses = userService.addressList(token);
        if (tMemberAddresses!=null){
            return AppResponse.ok(tMemberAddresses);
        }else{
            return AppResponse.fail("用户无地址");
        }
    }
}
