/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/07
 */

package com.tplink.cloud.apiserver.service;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.dto.UserInfoVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.dao.mapper.UserInfoMapper;
import com.tplink.cloud.apiserver.dao.mapper.UserMapper;
import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.model.UserInfo;
import com.tplink.cloud.apiserver.service.impl.UserServiceImpl;

import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:unit-test.xml" })
public class UserServiceTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserInfoMapper userInfoMapper;

    private UserVO userVO;

    private SimpleUserInfoVO simpleUserInfoVO;

    private UserInfo userInfo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        userVO = new UserVO();
        userVO.setEmail("hah@qq.com");
        userVO.setPassword("xx1234");

        simpleUserInfoVO = new SimpleUserInfoVO();
        simpleUserInfoVO.setAge("20");
        simpleUserInfoVO.setDisplayName("youyou");
        simpleUserInfoVO.setGender("M");

        userInfo = new UserInfo();
        userInfo.setAge(0);
        userInfo.setUserId(15);
        userInfo.setDisplayName("piu");

    }

    @Test
    public void saveUserAndReturnUserInfoVO() {

        UserInfoVO userInfoVO = userService.saveUser(userVO);
        verify(userMapper).saveUser(

                argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof User;
            }
        }));
        verify(userInfoMapper).saveUserInfo(argThat(new ArgumentMatcher<UserInfo>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserInfo;
            }
        }));
        assertNotNull(userInfoVO);
    }

    @Test
    public void updateUserInfoAndReturnUserInfoVO() {

        when(userInfoMapper.getUserInfo(anyInt())).thenReturn(userInfo);

        UserInfoVO userInfoVO = userService.updateUserInfo(15, simpleUserInfoVO);
        assertNotNull(userInfoVO);
        verify(userInfoMapper).updateUserInfo(argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }));
        verify(userInfoMapper).getUserInfo(anyInt());
    }

    @Test
    public void getUserInfo() {

        when(userInfoMapper.getUserInfo(anyInt())).thenReturn(userInfo);

        UserInfoVO userInfoVO = userService.getUserInfo(15);
        assertNotNull(userInfoVO);
        verify(userInfoMapper).getUserInfo(anyInt());
    }

    @Test
    public void checkUserIfExist() {

        when(userMapper.checkIfExist(anyString())).thenReturn(true);

        boolean result = userService.checkUserIfExist("xxx@qq.com");
        assertTrue(result);
        verify(userMapper).checkIfExist(anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void checkUserIfExist_Empty_Email() {
        userService.checkUserIfExist("");
    }

    @Test
    public void testGetUserByEmail() {

        when(userMapper.getUserByEmail(anyString())).thenReturn(new User());

        userService.getUserByEmail("xxx@qq.com");
        verify(userMapper).getUserByEmail(anyString());
    }
}
