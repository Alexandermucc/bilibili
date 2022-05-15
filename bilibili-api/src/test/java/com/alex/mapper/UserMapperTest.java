package com.alex.mapper;

import com.alex.BilibiliApplication;
import com.alex.dao.UserDao;
import com.alex.domain.User;
import com.alex.domain.UserInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.Date;

/**
 * @author Alexandermucc
 * @date 2022/05/14 16:32
 **/
@SpringBootTest
@ContextConfiguration(classes = BilibiliApplication.class)
public class UserMapperTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void test() {
        User dbUser = userDao.getUserByPhone("phone");
        System.out.println(dbUser);
    }

    @Test
    public void test1() {
        UserInfo userInfo = new UserInfo();
        userInfo.setUpdateTime(new Date());
        userInfo.setNick("alex");
        userInfo.setSign("");
        userInfo.setGender("0");
        userInfo.setBirth("1997-05-15");
        userDao.updateUserInfos(userInfo);
    }
}

