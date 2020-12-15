package com.offcn.pojo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel
public class RegisterVo {

    @ApiModelProperty("注册手机号")
    private String loginacct;//联系人手机
    @ApiModelProperty("登陆密码")
    private String userpswd;
    @ApiModelProperty("邮箱信息")
    private String email;
    @ApiModelProperty("手机验证码")
    private String code;
}
