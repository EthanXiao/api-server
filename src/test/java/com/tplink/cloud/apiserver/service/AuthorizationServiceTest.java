/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/07
 */

package com.tplink.cloud.apiserver.service;

import org.apache.ibatis.annotations.Param;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.tplink.cloud.apiserver.model.User;
import com.tplink.cloud.apiserver.service.impl.AuthorizationServiceImpl;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:spring/applicationContext.xml")
public class AuthorizationServiceTest {

    @InjectMocks
    private AuthorizationServiceImpl authorizationService;

    @Mock
    private IUserService userService;

    private final static String SALT_VALUE = "NjhiNmM2NzUxODE3NGYyM2E2OTA5YzRhODQ3N2YxZDg";

    private final static String PASSWORD = "D154CCB07C88F16EE6AAAB51EFA5D10453FBE3549434242B34408909D649BEE1";  //password:1234
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

    }

    @Test
    public void attemptAuthentication_Success(){

        User user = new User();
        user.setPassword(PASSWORD);
        user.setSecretKey(SALT_VALUE);

        when(userService.getUserByEmail(anyString())).thenReturn(user);

        Integer i = authorizationService.attemptAuthentication("xxx@qq.com","1234");
        assertNotNull(i);
        verify(userService).getUserByEmail(anyString());
    }
    @Test
    public void attemptAuthentication_UserNotExist() {

        User user = new User();
        user.setPassword(PASSWORD);
        user.setSecretKey(SALT_VALUE);

        when(userService.getUserByEmail(anyString())).thenReturn(user);

        Integer i = authorizationService.attemptAuthentication("xxx@qq.com", "123");
        assertNull(i);
        verify(userService).getUserByEmail(anyString());
    }
}
