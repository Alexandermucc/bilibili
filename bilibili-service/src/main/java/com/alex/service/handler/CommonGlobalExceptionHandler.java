package com.alex.service.handler;

import com.alex.domain.JsonResponse;
import com.alex.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Alexandermucc
 * @date 2022/05/14 13:46
 **/
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE) // 最高优先级
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request,
                                                       Exception e) {
        String errorMessage = e.getMessage();
        if (e instanceof ConditionException) {
            // 可以通过ConditionException定制化code状态码
            String errorCode =((ConditionException) e).getCode();
            return new JsonResponse<>(errorCode,errorMessage);
        } else {
            // 不能定制化code
            return new JsonResponse<>("500",errorMessage);
        }

    }

}

