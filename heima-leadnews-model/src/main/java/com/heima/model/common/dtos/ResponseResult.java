package com.heima.model.common.dtos;

import com.alibaba.fastjson.JSON;
import com.heima.model.common.enums.AppHttpCodeEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 通用的结果返回类
 * @param <T>
 */
public class ResponseResult<T> implements Serializable {

    private String host;

    private Integer code;

    private String errorMessage;

    private T data;

    //无参构造方法，将状态码默认设置为200。
    public ResponseResult() {
        this.code = 200;
    }

    //构造方法，接收状态码和数据。
    public ResponseResult(Integer code, T data) {
        this.code = code;
        this.data = data;
    }

    //构造方法，接收状态码、错误信息和数据。
    public ResponseResult(Integer code, String msg, T data) {
        this.code = code;
        this.errorMessage = msg;
        this.data = data;
    }

    //构造方法，接收状态码和错误信息。
    public ResponseResult(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
    }


    //静态方法，用于创建一个错误响应。
    public static ResponseResult errorResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.error(code, msg);
    }

    //静态方法，用于创建并返回一个成功的响应。
    public static ResponseResult okResult(int code, String msg) {
        ResponseResult result = new ResponseResult();
        return result.ok(code, null, msg);
    }

    //静态方法，用于创建一个成功的响应并包含数据。
    public static ResponseResult okResult(Object data) {
        ResponseResult result = setAppHttpCodeEnum(AppHttpCodeEnum.SUCCESS, AppHttpCodeEnum.SUCCESS.getErrorMessage());
        if(data!=null) {
            result.setData(data);
        }
        return result;
    }

    //静态方法，用于创建一个基于枚举的错误响应。
    public static ResponseResult errorResult(AppHttpCodeEnum enums){
        return setAppHttpCodeEnum(enums,enums.getErrorMessage());
    }

    //静态方法，用于创建一个基于枚举的错误响应，并允许自定义错误信息。
    public static ResponseResult errorResult(AppHttpCodeEnum enums, String errorMessage){
        return setAppHttpCodeEnum(enums,errorMessage);
    }

    //静态方法，用于创建一个基于枚举的成功响应。没有使用到。
    public static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums){
        return okResult(enums.getCode(),enums.getErrorMessage());
    }


    //私有静态方法，用于创建一个基于枚举的响应，并允许自定义错误信息。
    private static ResponseResult setAppHttpCodeEnum(AppHttpCodeEnum enums, String errorMessage){
        return okResult(enums.getCode(),errorMessage);
    }

    //实例方法，用于设置错误码和错误信息。
    public ResponseResult<?> error(Integer code, String msg) {
        this.code = code;
        this.errorMessage = msg;
        return this;
    }

    //实例方法，用于设置成功响应的状态码和数据。
    public ResponseResult<?> ok(Integer code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    //实例方法，用于设置成功响应的状态码、数据和消息。
    public ResponseResult<?> ok(Integer code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.errorMessage = msg;
        return this;
    }

    //实例方法，用于设置成功响应的数据。
    public ResponseResult<?> ok(T data) {
        this.data = data;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }


    public static void main(String[] args) {
        //前置
//        AppHttpCodeEnum success = AppHttpCodeEnum.SUCCESS;
//        System.out.println(success.getCode());
//        System.out.println(success.getErrorMessage());

        //查询一个对象
        /*Map map = new HashMap();
        map.put("name","zhangsan");
        map.put("age",18);
        ResponseResult result = ResponseResult.okResult(map);
        System.out.println(JSON.toJSONString(result));*/


        //新增，修改，删除  在项目中统一返回成功即可
       /* ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SUCCESS);
        System.out.println(JSON.toJSONString(result));*/


        //根据不用的业务返回不同的提示信息  比如：当前操作需要登录、参数错误
        /*ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.NEED_LOGIN);
        System.out.println(JSON.toJSONString(result));*/

        //查询分页信息
        PageResponseResult responseResult = new PageResponseResult(1,5,50);
        List list = new ArrayList();
        list.add("itcast");
        list.add("itheima");
        responseResult.setData(list);
        System.out.println(JSON.toJSONString(responseResult));

    }

}
