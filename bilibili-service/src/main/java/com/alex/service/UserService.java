package com.alex.service;

import com.alex.dao.UserDao;
import com.alex.domain.User;
import com.alex.domain.UserInfo;
import com.alex.domain.constant.UserConstant;
import com.alex.domain.exception.ConditionException;
import com.alex.service.util.MD5Util;
import com.alex.service.util.RSAUtil;
import com.alex.service.util.TokenUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Alexandermucc
 * @date 2022/05/14 14:18
 **/

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public void addUser(User user) {
        String phone = user.getPhone();

        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = userDao.getUserByPhone(phone);

        if (dbUser != null) {
            throw new ConditionException("该手机号已经被注册！");
        }

        Date now = new Date();
        String salt = String.valueOf(now.getTime());

        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.encrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        user.setSalt(salt);
        user.setPassword(password);
        user.setCreateTime(now);

        userDao.addUser(user);

        // 添加用户信息
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(user.getId());
        userInfo.setNick(UserConstant.DEFAULT_NICK);
        userInfo.setBirth(UserConstant.DEFAULT_BIRTH);
        userInfo.setGender(UserConstant.GENDER_MALE);
        userInfo.setCreateTime(now);
        userDao.addUserInfo(userInfo);
    }

    public User getUserByPhone(String phone) {
        return userDao.getUserByPhone(phone);
    }

    public String login(User user) {
        String phone = user.getPhone();

        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }

        User dbUser = userDao.getUserByPhone(phone);

        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        String password = dbUser.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.encrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword,salt,"UTF-8");

        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }

        TokenUtil tokenUtil = new TokenUtil();
        return tokenUtil.generateToken(dbUser.getId());
    }
}
