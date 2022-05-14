package com.alex.api;

import com.alex.domain.JsonResponse;
import com.alex.domain.User;
import com.alex.service.UserService;
import com.alex.service.util.RSAUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexandermucc
 * @date 2022/05/14 14:19
 **/
@RestController
public class UserApi {

    @Autowired
    private UserService userService;


    /**
     * 获取RSA公钥
     * @return
     */
    @GetMapping("/rsa-pks")
    public JsonResponse<String> getRsaPublicKey() {
        String pk = RSAUtil.getPublicKeyStr();
        return new JsonResponse<>(pk);
    }

    /**
     * 用户注册
     * @param user
     * @return
     */
    @PostMapping("/users")
    public JsonResponse<String> addUsers(@RequestBody User user) {
        userService.addUser(user);
        return JsonResponse.success();
    }

    /**
     * 用户登录
     * @param user
     * @return
     */
    @PostMapping("/user-token")
    public JsonResponse<String> login(@RequestBody User user) {
        String token = userService.login(user);
        return JsonResponse.success(token);
    }
}

