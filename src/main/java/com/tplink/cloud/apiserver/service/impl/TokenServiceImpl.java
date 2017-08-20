/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service.impl;

import java.util.Set;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.tplink.cloud.apiserver.dto.TokenVO;
import com.tplink.cloud.apiserver.service.ITokenService;
import com.tplink.cloud.apiserver.dao.RedisDao;
import com.tplink.cloud.apiserver.util.UUIDGenerator;

@Service
public class TokenServiceImpl implements ITokenService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TokenServiceImpl.class);

    @Resource
    private RedisDao redisDao;

    @Value("${redis.expiredIn}")
    private int expiredIn;

    @Value("${redis.tokenSize}")
    private int tokenSize;

    @Override
    public TokenVO saveToken(int userId) {
        LOGGER.debug("enter saveToken method, userId:[{}]", userId);

        Set<String> tokens = redisDao.zrangBySore(String.valueOf(userId), System.currentTimeMillis(),
                                                  System.currentTimeMillis() - expiredIn);
        String token;
        if (tokens.size() > tokenSize) {

            redisDao.zremrangeByRank(String.valueOf(userId), 0, -tokenSize);
            token = UUIDGenerator.generateRandomToken();

            redisDao.set(token, userId, expiredIn);
            redisDao.zadd(String.valueOf(userId), System.nanoTime(), token);
        } else {
            token = UUIDGenerator.generateRandomToken();
            redisDao.set(token, userId, expiredIn);
            redisDao.zadd(String.valueOf(userId), System.nanoTime(), token);
        }

        TokenVO tokenVO = new TokenVO();
        tokenVO.setUserId(userId);
        tokenVO.setToken(token);
        tokenVO.setExpiresIn(expiredIn);

        LOGGER.debug("save token success. tokenVO:[{}]", tokenVO);
        return tokenVO;
    }

    @Override
    public String checkTokenIfExist(String token) {
        LOGGER.debug("enter checkTokenIfExist method, token:[{}]", token);
        return redisDao.get(token);
    }

    @Override
    public boolean delToken(String token) {
        LOGGER.debug("enter delToken method, token:[{}]", token);
        String userId = redisDao.get(token);
        return (redisDao.zrem(userId, token) > 0) && (redisDao.del(token) > 0);
    }

}
