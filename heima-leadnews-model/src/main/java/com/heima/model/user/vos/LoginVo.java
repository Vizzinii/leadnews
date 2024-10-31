package com.heima.model.user.vos;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginVo implements Serializable {

    private Integer id;

    private String name;

    private String phone;

    private String token;
}
