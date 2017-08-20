/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service;

import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.dto.UserInfoVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.model.UserInfo;

import java.util.Map;

/**
 *
 */
public interface IUserService {

    /**
     * save user and userInfo
     * @param user
     * @return userInfoVO
     */
    UserInfoVO saveUser(UserVO user);

    /**
     * update userInfo according to simpleUserInfoVO
     * @param userId
     * @param simpleUserInfoVO
     * @return
     */
    UserInfoVO updateUserInfo(Integer userId, SimpleUserInfoVO simpleUserInfoVO);

    /**
     * get userInfo by userId
     * @param userId
     * @return
     */
    UserInfoVO getUserInfo(Integer userId);

    /**
     * check if email is exist
     * @param email
     * @return
     */
    boolean checkUserIfExist(String email);

    /**
     * get user by email
     * @param email
     * @return
     */
    User getUserByEmail(String email);
}
