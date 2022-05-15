package com.alex.api;

import com.alex.domain.UserFollowing;
import com.alex.service.FollowingGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Alexandermucc
 * @date 2022/05/15 13:39
 **/
@RestController
public class FollowingGroupApi {

    @Autowired
    private FollowingGroupService followingGroupService;


}

