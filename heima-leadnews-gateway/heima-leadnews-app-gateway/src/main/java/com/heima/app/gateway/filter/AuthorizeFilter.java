package com.heima.app.gateway.filter;

import com.heima.app.gateway.util.AppJwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component //必须被spring进行管理
@Slf4j
public class AuthorizeFilter implements Ordered, GlobalFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 1. 获取request和response对象，从而获取url
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        RequestPath path = request.getPath();

        // 2.判断是否需要做登录拦截（看request的请求路径与yaml文件里配置的路径是否一致
        if(request.getURI().getPath().contains("/login")){
            return chain.filter(exchange);
        }
        // 3. 获取token
        String token = headers.getFirst("token");

        // 4. 判断token是否存在
        ServerHttpResponse response = exchange.getResponse();
        if (StringUtils.isBlank(token)){
            response.setStatusCode(HttpStatus.UNAUTHORIZED); //设置返回给前端的状态码
            return response.setComplete(); //直接结束本次请求
        }

        try {
            Claims claimsBody = AppJwtUtil.getClaimsBody(token);

            // 6. 判断token是否过期
            //      -1：有效，0：有效，1：过期，2：过期
            int result = AppJwtUtil.verifyToken(claimsBody);
            if (result == 1 || result == 2){
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return response.setComplete();
            }
        }catch (Exception e){
            e.printStackTrace();
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return response.setComplete();
        }
        // 5. 判断token是否有效

        // 6. 放行
        return chain.filter(exchange);
    }

    /**
     * 优先级设置：值越小，优先级越高
     * @return
     */
    @Override
    public int getOrder() {
        return 0;
    }
}
