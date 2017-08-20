/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/31
 */

package com.tplink.cloud.apiserver.dao;

import com.tplink.cloud.apiserver.dao.mapper.UserMapper;
import com.tplink.cloud.apiserver.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = {"classpath:unit-test.xml"})
public class UserMapperTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserMapperTest.class);

    @Resource
    private UserMapper userMapper;

    private User user;

    @Before
    public void setup(){
        user = new User();
        user.setEmail("x@qq.com");
        user.setPassword("1234");
        user.setSecretKey("1234");
    }
    @Test
    public void saveAndGetUser(){
        userMapper.saveUser(user);
        User testUser = userMapper.getUserByEmail(user.getEmail());
        Assert.assertNotNull(testUser);
    }

    @Test
    public void checkIfExists(){
        User user1 = new User();
        user1.setUserId(1);
        user1.setEmail("hyh@qq.com");
        user1.setPassword("1234");
        user1.setSecretKey("1234");
        userMapper.saveUser(user1);
        boolean result = userMapper.checkIfExist(user1.getEmail());
        Assert.assertTrue(result);
    }
}
