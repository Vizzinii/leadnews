package com.heima.user.controller.v1;

import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.user.dtos.LoginDto;
import com.heima.user.service.ApUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController  //在使用 @RestController 注解标记的类中，每个方法的返回值都会以 JSON 或 XML 的形式直接写入 HTTP 响应体中，相当于在每个方法上都添加了 @ResponseBody 注解。
@RequestMapping("/api/v1/login")  //用于将任意HTTP 请求映射到控制器方法上。
@Api(value = "APP端用户登录",tags = "APP端用户登录")
@RequiredArgsConstructor
public class ApUserLoginController {

    private final ApUserService apUserService;

    @ApiOperation("用户登录方法")
    @PostMapping("/login_auth")
    public ResponseResult login(@RequestBody LoginDto loginDto) {
        return apUserService.login(loginDto);
    }
}
