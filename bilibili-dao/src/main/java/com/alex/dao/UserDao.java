package com.alex.dao;

import com.alex.domain.User;
import com.alex.domain.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author Alexandermucc
 * @date 2022/5/14 - 14:20 - 周六
 **/
@Mapper
public interface UserDao {
    User getUserByPhone(String phone);

    Integer addUser(User user);

    Integer addUserInfo(UserInfo userInfo);
}
