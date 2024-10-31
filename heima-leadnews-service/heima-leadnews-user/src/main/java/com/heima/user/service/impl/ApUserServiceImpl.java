package com.heima.user.service.impl;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heima.model.common.dtos.ResponseResult;
import com.heima.model.common.enums.AppHttpCodeEnum;
import com.heima.model.user.dtos.LoginDto;
import com.heima.model.user.pojos.ApUser;
import com.heima.user.mapper.ApUserMapper;
import com.heima.user.service.ApUserService;
import com.heima.utils.common.AppJwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional //事务管理那注解，一个@Transactional注解下的类是一个原子操作
@Slf4j
public class ApUserServiceImpl extends ServiceImpl<ApUserMapper, ApUser> implements ApUserService {

    @Override
    public ResponseResult login(LoginDto loginDto) {
        // 1.正常登录，需要用户名和密码
        if(StringUtils.isNotBlank(loginDto.getPhone()) && StringUtils.isNotBlank(loginDto.getPassword())){
            // 1.1 根据输入的手机号查询数据库内用户信息
            ApUser user = getOne(Wrappers.<ApUser>lambdaQuery().eq(ApUser::getPhone, loginDto.getPhone()));
            if(user == null){
                return ResponseResult.errorResult(AppHttpCodeEnum.DATA_NOT_EXIST,"小杜提醒您：用户信息不存在");
            }

            // 1.2 查询得到用户的salt
            String salt = user.getSalt();

            // 1.3 根据用户输入的密码+查询得到的salt，通过MD5得到加密值
            String password_insert = loginDto.getPassword();
            String password_generate = DigestUtils.md5DigestAsHex((password_insert + salt).getBytes());

            // 1.4 把加密值与数据库内用户的加密值比对，得到密码正确或错误的结果
            String password_from_sql = user.getPassword();
            if(!password_generate.equals(password_from_sql)){
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR,"小杜提醒您：密码错啦");
            }

            // 1.5 返回数据，是由user和jwt组成的LoginVo
            Integer userId = user.getId();
            String token = AppJwtUtil.getToken(userId.longValue());
            Map<String,Object> map = new HashMap<>();
            user.setSalt("");
            user.setPassword("");
            map.put("user",user);
            map.put("token",token);
            return ResponseResult.okResult(map);
            // 若修改此方法的返回值，可以如下返回一个LoginVo对象
            //return LoginVo
            //       .build()……
        }else {
            // 2.游客登录
            Map<String,Object> map = new HashMap<>();
            map.put("token",AppJwtUtil.getToken(0L));
            return ResponseResult.okResult(map);
        }
    }
}
