/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service.impl;

import javax.annotation.Resource;

import com.google.common.base.Strings;
import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tplink.cloud.apiserver.common.Constants;
import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.dto.UserInfoVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.dao.mapper.UserInfoMapper;
import com.tplink.cloud.apiserver.dao.mapper.UserMapper;
import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.model.UserInfo;
import com.tplink.cloud.apiserver.service.IUserService;
import com.tplink.cloud.apiserver.util.BeanUtil;
import com.tplink.cloud.apiserver.util.EncryptionUtil;
import com.tplink.cloud.apiserver.util.UUIDGenerator;

@Service
public class UserServiceImpl implements IUserService {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserInfoVO saveUser(UserVO userVO) {
        LOGGER.debug("enter saveUser method, email:[{}], displayName:[{}]", userVO.getEmail(), userVO.getDisplayName());

        //check param
        Preconditions.checkNotNull(userVO, "userVO is null.");
        if (!userVO.checkValid()) {
            throw new IllegalArgumentException("userVO is illegal.");
        }

        //save user
        User user = BeanUtil.copyFrom(userVO, User.class);
        user.setSecretKey(UUIDGenerator.generateEncodingSecretKey());
        user.setPassword(EncryptionUtil.getSHA256(user.getPassword() + user.getSecretKey()));
        userMapper.saveUser(user);
        LOGGER.debug("save user success, user:[{}]", user);

        //save userInfo, set default information
        UserInfo userInfo = BeanUtil.copyFrom(user, UserInfo.class);
        if (Strings.isNullOrEmpty(userVO.getDisplayName())) {
            userInfo.setDisplayName(userVO.getEmail());
        } else {
            userInfo.setDisplayName(userVO.getDisplayName());
        }
        userInfo.setGender(Constants.DEFAULT_GENDER);
        userInfo.setAge(Constants.DEFAULT_AGE);
        userInfoMapper.saveUserInfo(userInfo);
        LOGGER.debug("save userInfo success, userInfo:[{}]", userInfo);

        return BeanUtil.copyFrom(userInfo, UserInfoVO.class);
    }

    @Override
    public UserInfoVO updateUserInfo(final Integer userId, SimpleUserInfoVO simpleUserInfoVO) {
        LOGGER.debug("enter updateUserInfo method, userId:[{}], userInfoVO:[{}]", userId, simpleUserInfoVO);

        if (null == userId || !simpleUserInfoVO.isValid()) {
            throw new IllegalArgumentException("update userInfo fail. userId is null.");
        }
        simpleUserInfoVO.setUserId(userId);
        userInfoMapper.updateUserInfo(simpleUserInfoVO);
        return BeanUtil.copyFrom(userInfoMapper.getUserInfo(userId), UserInfoVO.class);
    }

    @Override
    public UserInfoVO getUserInfo(final Integer userId) {
        LOGGER.debug("enter getUserInfo method", userId);
        if (null == userId) {
            throw new IllegalArgumentException("get userId fail. userId is null.");
        }

        return BeanUtil.copyFrom(userInfoMapper.getUserInfo(userId), UserInfoVO.class);
    }

    @Override
    public boolean checkUserIfExist(String email) {
        LOGGER.debug("enter checkUserIfExist method, email:[{}]", email);
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("param illegal, user email is null");
        }
        return userMapper.checkIfExist(email);
    }

    @Override
    public User getUserByEmail(String email) {
        LOGGER.debug("enter getUserByEmail method, email:[{}]", email);
        if (Strings.isNullOrEmpty(email)) {
            throw new IllegalArgumentException("param illegal, user email is null");
        }
        return userMapper.getUserByEmail(email);
    }
}
