package com.heima.model.user.dtos;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class LoginDto {

    /**
     * 手机号
     */
    @ApiModelProperty(value = "账号（默认为手机号）",required = true)
    private String phone;

    /**
     * 密码
     */
    @ApiModelProperty(value = "用户登录时键入的密码",required = true) // required = true 代表这一项是必须输入的
    private String password;
}
