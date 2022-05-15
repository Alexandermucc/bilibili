package com.alex.service.util;

import com.alex.domain.exception.ConditionException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Alexandermucc
 * @date 2022/05/14 15:13
 **/

// 生成令牌
public class TokenUtil {

    private static final String ISSUER = "签发者";


    public static String generateToken(Long userId) throws Exception {
        // 设置秘钥
        Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
        // 用于设置过期时间
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        // 设置过期时间
        calendar.add(Calendar.HOUR, 10);
        return JWT.create()
                // 添加唯一身份标识
                .withKeyId(String.valueOf(userId))
                // 添加签发者
                .withIssuer(ISSUER)
                .withExpiresAt(calendar.getTime())
                // 生成签名
                .sign(algorithm);
    }


    /**
     * 验证token
     * @param token
     * @return userId
     */
    public static Long verifyToken(String token){
        // 这里不能直接抛出异常,
        // 为什么,原因在于该方法是用于验证身份,如果存在token失效等问题,用户的体验不好,不能进行后续的操作

        // 设置秘钥
        try {
            Algorithm algorithm = Algorithm.RSA256(RSAUtil.getPublicKey(), RSAUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            String userId = decodedJWT.getKeyId();

            return Long.valueOf(userId);
        } catch (TokenExpiredException e) {
            throw new ConditionException("555","token过期！");
        } catch (Exception e) {
            throw new ConditionException("非法用户token！");
        }

    }
}

