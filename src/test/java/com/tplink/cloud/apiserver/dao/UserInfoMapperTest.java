/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/31
 */

package com.tplink.cloud.apiserver.dao;

import com.tplink.cloud.apiserver.dao.mapper.UserInfoMapper;
import com.tplink.cloud.apiserver.dao.mapper.UserMapper;
import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.model.UserInfo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:unit-test.xml")
public class UserInfoMapperTest {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    private User user;

    private UserInfo userInfo;

    private SimpleUserInfoVO simpleUserInfoVO;

    @Before
    public void setup(){
        user = new User();
        user.setUserId(1);
        user.setEmail("xx@qq.com");
        user.setPassword("1234");
        user.setSecretKey("1234");

        userInfo = new UserInfo();
        userInfo.setUserId(15);
        userInfo.setDisplayName("xx@qq.com");
        userInfo.setEmail("xx@qq.com");
        userInfo.setAge(20);
        userInfo.setGender("M");

        simpleUserInfoVO = new SimpleUserInfoVO();
        simpleUserInfoVO.setUserId(15);
        simpleUserInfoVO.setGender("F");
        simpleUserInfoVO.setDisplayName("asd");
        simpleUserInfoVO.setAge("20");
    }

    @Test
    public void saveAndGetUserInfo(){
        userMapper.saveUser(user);
        userInfoMapper.saveUserInfo(userInfo);
        UserInfo otherInfo = userInfoMapper.getUserInfo(userInfo.getUserId());
        Assert.assertNotNull(otherInfo);
    }

    @Test
    public void updateAndGetUserInfo(){
        userInfo.setInfoId(2);
        userInfo.setUserId(20);
        userInfoMapper.saveUserInfo(userInfo);
        userInfoMapper.updateUserInfo(simpleUserInfoVO);
        UserInfo otherInfo = userInfoMapper.getUserInfo(20);
        Assert.assertNotNull(otherInfo);
    }

    @Test
    public void getUserInfo(){
        UserInfo userInfo = userInfoMapper.getUserInfo(15);
        Assert.assertNotNull(userInfo);
    }
}
