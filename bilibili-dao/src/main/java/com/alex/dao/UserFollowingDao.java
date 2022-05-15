package com.alex.dao;

import com.alex.domain.UserFollowing;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Alexandermucc
 * @date 2022/5/15 - 13:40 - 周日
 **/
@Mapper
public interface UserFollowingDao {

    // 多个参数是 使用@Param注解
    Integer deleteUserFollowing(@Param("userId") Long userId,
                                @Param("followingId") Long followingId);

    Integer addUserFollowing(UserFollowing userFollowing);

    List<UserFollowing> getUserFollowingGroups(Long userId);

    List<UserFollowing> getUserFans(Long userId);
}
