package com.alex.service;

import com.alex.dao.UserFollowingDao;
import com.alex.domain.FollowingGroup;
import com.alex.domain.User;
import com.alex.domain.UserFollowing;
import com.alex.domain.UserInfo;
import com.alex.domain.constant.UserConstant;
import com.alex.domain.exception.ConditionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Alexandermucc
 * @date 2022/05/15 13:40
 **/
@Service
public class UserFollowingService {

    @Autowired
    private UserFollowingDao userFollowingDao;
    @Autowired
    private FollowingGroupService followingGroupService;

    @Autowired
    private UserService userService;

    /**
     * 添加用户关注
     *
     * @param userFollowing
     */
    @Transactional // 添加事务处理,失败时可以回滚
    public void addUserFollowings(UserFollowing userFollowing) {
        Long groupId = userFollowing.getGroupId();


        // 没理解啊
        if (groupId == null) {
            FollowingGroup followingGroup = followingGroupService.getByType(UserConstant.USER_FOLLOWING_GROUP_DEFAULT);
            // 给关注的用户添加默认分组
            userFollowing.setGroupId(followingGroup.getId());
        } else {
            FollowingGroup followingGroup = followingGroupService.getById(groupId);
            if (followingGroup == null) {
                throw new ConditionException("关注的分组不存在");
            }
        }

        // 判断关注的用户是否存在
        Long followingId = userFollowing.getFollowingId();

        User user = userService.getUserById(followingId);

        if (user == null) {
            throw new ConditionException("关注的用户不存在");
        }


        /*删除与更新== 更新*/
        // 用于删除 userId与followingId关联的数据
        userFollowingDao.deleteUserFollowing(userFollowing.getUserId(), followingId);
        // 用于添加 用户与关注的关系
        userFollowing.setCreateTime(new Date());
        userFollowingDao.addUserFollowing(userFollowing);
    }


    public List<FollowingGroup> getUserFollowingGroups(Long userId) {
        // 1. 获取关注列表
        List<UserFollowing> userFollowingList = userFollowingDao.getUserFollowingGroups(userId);
        Set<Long> followingIdSet = userFollowingList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());

        // 2. 根据关注用户id查询关注用户的基本信息
        List<UserInfo> userInfoList = new ArrayList<>();
        if (followingIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(followingIdSet);

        }

        for (UserFollowing userFollowing : userFollowingList) {
            for (UserInfo userInfo : userInfoList) {
                if (userFollowing.getFollowingId().equals(userInfo.getUserId())) {
                    userFollowing.setUserInfo(userInfo);
                }
            }
        }
        // 3. 将关注用户按关注分组进行分类
        List<FollowingGroup> groupList = followingGroupService.getByUserId(userId);
        FollowingGroup allGroup = new FollowingGroup();
        allGroup.setName(UserConstant.USER_FOLLOWING_GROUP_ALL_NAME);
        allGroup.setFollowingUserInfoList(userInfoList);

        List<FollowingGroup> result = new ArrayList<>();
        result.add(allGroup);

        for (FollowingGroup group : groupList) {
            List<UserInfo> infoList = new ArrayList<>();
            for (UserFollowing userFollowing : userFollowingList) {
                if (group.getId().equals(userFollowing.getGroupId())) {
                    infoList.add(userFollowing.getUserInfo());
                }
            }
            group.setFollowingUserInfoList(infoList);
            result.add(group);
        }

        return result;
    }


    public List<UserFollowing> getUserFans(Long userId) {
        //第一步:获取当前用户的粉丝列表
        List<UserFollowing> fanList = userFollowingDao.getUserFans(userId);
        //第二步:根据粉丝的用户id查询基本信息
        Set<Long> fanIdSet = fanList.stream().map(UserFollowing::getFollowingId).collect(Collectors.toSet());
        List<UserInfo> userInfoList = new ArrayList<>();
        if (fanIdSet.size() > 0) {
            userInfoList = userService.getUserInfoByUserIds(fanIdSet);
        }

        //第三步:查询当前用户是否已经关注该粉丝
        List<UserFollowing> followingList = userFollowingDao.getUserFollowingGroups(userId);
        for (UserFollowing fan : fanList) {
            for (UserInfo userInfo : userInfoList) {
                // 是否关注的初始化
                if (fan.getUserId().equals(userInfo.getUserId())) {
                    userInfo.setFollowed(false);
                    fan.setUserInfo(userInfo);
                }
            }

            for (UserFollowing following : followingList) {
                // 互粉的状态
                if (following.getFollowingId().equals(fan.getUserId())) {
                    fan.getUserInfo().setFollowed(true);
                }
            }

        }

        return fanList;
    }

}

