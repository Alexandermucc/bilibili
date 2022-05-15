package com.alex.api.support;

import com.alex.domain.exception.ConditionException;
import com.alex.service.util.TokenUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @author Alexandermucc
 * @date 2022/05/14 15:56
 **/

/**
 * 用于支撑api运行的模块
 */
@Component
public class UserSupport {


    public Long getCurrentUserId() {
        // springboot抓取请求上下文的方法
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);

        // userId是大于0的
        if (userId < 0) {
            throw new ConditionException("非法用户");
        }

        return userId;
    }

}

