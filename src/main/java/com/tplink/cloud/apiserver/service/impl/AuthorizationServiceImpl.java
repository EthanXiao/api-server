/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service.impl;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.service.IAuthorizationService;
import com.tplink.cloud.apiserver.service.IUserService;
import com.tplink.cloud.apiserver.util.EncryptionUtil;

@Service
public class AuthorizationServiceImpl implements IAuthorizationService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorizationServiceImpl.class);

    @Resource
    private IUserService userService;

    @Override
    public Integer attemptAuthentication(String email, String password) {

        LOGGER.debug("enter attemptAuthentication method, email:[{}]", email);

        User user = userService.getUserByEmail(email);
        if (null == user) {
            LOGGER.debug("user authentication failed, user not exists. email:[{}]", email);
            return null;
        }
        if (EncryptionUtil.getSHA256(password + user.getSecretKey()).equals(user.getPassword())) {
            LOGGER.debug("user authentication is successful, email:[{}]", email, password);
            return user.getUserId();
        }
        LOGGER.debug("user authentication failed, email:[{}]", email);
        return null;
    }
}
