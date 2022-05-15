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
import java.util.List;
import java.util.Set;

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
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        user.setSalt(salt);
        user.setPassword(md5Password);
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

    public String login(User user) throws Exception {
        String phone = user.getPhone();

        if (StringUtils.isNullOrEmpty(phone)) {
            throw new ConditionException("手机号不能为空！");
        }
        User dbUser = userDao.getUserByPhone(phone);

        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        // user是前端传递过来的 password是经过RSA加密,现在需要将该password解密并使用MD5加密与数据库中进行比较,如果比较成功则代表密码正确
        if (StringUtils.isNullOrEmpty(user.getPassword())) {
            throw new ConditionException("该用户密码为空");
        }


        String password = user.getPassword();
        String rawPassword;
        try {
            rawPassword = RSAUtil.decrypt(password);
        } catch (Exception e) {
            throw new ConditionException("密码解密失败！");
        }

        String salt = dbUser.getSalt();
        String md5Password = MD5Util.sign(rawPassword, salt, "UTF-8");

        if (!md5Password.equals(dbUser.getPassword())) {
            throw new ConditionException("密码错误！");
        }

        return TokenUtil.generateToken(dbUser.getId());
    }


    /**
     * 更新用户 email、phone、password
     * 需要判断是否重复【未实现】
     * @param user
     */
    public void updateUser(User user) {
        Long id = user.getId();
        User dbUser = userDao.getUserById(id);

        if (dbUser == null) {
            throw new ConditionException("当前用户不存在！");
        }

        if (!StringUtils.isNullOrEmpty(user.getPassword())) {
            String rawPassword = null;
            try {
                rawPassword = RSAUtil.decrypt(user.getPassword());
            } catch (Exception e) {
                throw new ConditionException("密码解密失败！");
            }
            String md5Password = MD5Util.sign(rawPassword, dbUser.getSalt(), "UTF-8");
            user.setPassword(md5Password);
        }

        user.setUpdateTime(new Date());
        userDao.updateUsers(user);
    }

    public User getUserInfo(Long userId) {
        User user = userDao.getUserById(userId);
        UserInfo userInfo = userDao.getUserInfoByUserId(userId);
        user.setUserInfo(userInfo);
        return user;
    }



    /**
     * 更新用户信息
     *
     * @param userInfo
     */
    public void updateUserInfos(UserInfo userInfo) {
        userInfo.setUpdateTime(new Date());
        Integer res = userDao.updateUserInfos(userInfo);
        System.out.println("userDao.updateUserInfos(userInfo);的返回结果为\n"+res);
    }


    public User getUserById(Long followingId) {
        return userDao.getUserById(followingId);
    }

    public List<UserInfo> getUserInfoByUserIds(Set<Long> userIdList) {
        return userDao.getUserInfoByUserIds(userIdList);
    }
}

