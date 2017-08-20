/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/07
 */

package com.tplink.cloud.apiserver.controller;

import org.apache.ibatis.exceptions.PersistenceException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.dto.UserInfoVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.exception.BeanUtilException;
import com.tplink.cloud.apiserver.interceptor.AuthorizationInterceptor;
import com.tplink.cloud.apiserver.service.ITokenService;
import com.tplink.cloud.apiserver.service.IUserService;

@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest extends ControllerTestBase {

    private MockMvc mockMvc;

    @InjectMocks
    private UserController userController;

    @Mock
    private IUserService userService;

    @Mock
    private ITokenService tokenService;

    private UserInfoVO userInfoVO;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        AuthorizationInterceptor authorizationInterceptor = new AuthorizationInterceptor();
        ReflectionTestUtils.setField(authorizationInterceptor, "tokenService", tokenService);

        this.mockMvc =
                MockMvcBuilders.standaloneSetup(userController).addInterceptors(authorizationInterceptor).build();

        UserVO userVO = new UserVO();
        userVO.setEmail("xx@qq.com");
        userVO.setPassword("xx1234");

        userInfoVO = new UserInfoVO();
        userInfoVO.setDisplayName("xx@qq.com");
        userInfoVO.setEmail("xx@qq.com");
        userInfoVO.setAge(0);
        userInfoVO.setGender("M");
    }

    @Test
    public void ping() throws Exception {
        mockMvc.perform(get("/ping")).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void register_Success() throws Exception {
        when(userService.checkUserIfExist("xx@qq.com")).thenReturn(false);
        when(userService.saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }))).thenReturn(userInfoVO);

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";
        mockMvc.perform(post("/user").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                     .content(requestBody)
                                     .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(200))
               .andExpect(jsonPath("$.errMsg").value("request success"));

        verify(userService).checkUserIfExist(anyString());
        verify(userService).saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }));
    }

    @Test
    public void register_ParamIllegal() throws Exception {

        String requestBody = "{\"email\":xx.com, \"password\":\"1234\"}";

        mockMvc.perform(post("/user").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                     .content(requestBody)
                                     .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(400))
               .andExpect(jsonPath("$.errMsg").value("request illegal, param illegal or lack of necessary param"))
               .andDo(print());

        verify(userService, never()).checkUserIfExist(anyString());
        verify(userService, never()).saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }));
    }

    @Test
    public void register_UserAlreadyExist() throws Exception {

        when(userService.checkUserIfExist("xxx@qq.com")).thenReturn(true);

        String requestBody = "{\"email\":\"xxx@qq.com\", \"password\":\"xx1234\"}";
        mockMvc.perform(post("/user").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                     .content(requestBody)
                                     .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(409))
               .andExpect(jsonPath("$.errMsg").value("user already exists"))
               .andDo(print());

        verify(userService).checkUserIfExist(anyString());
        verify(userService, never()).saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void register_ThrowBeanUtilException() throws Exception {

        when(userService.checkUserIfExist("xx@qq.com")).thenReturn(false);
        when(userService.saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }))).thenThrow(BeanUtilException.class);

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";

        mockMvc.perform(post("/user").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                     .content(requestBody)
                                     .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(500))
               .andExpect(jsonPath("$.errMsg").value("server error"))
               .andDo(print());

        verify(userService).checkUserIfExist(anyString());
        verify(userService).saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }));
    }

    @Test
    public void register_ThrowException() throws Exception {

        when(userService.checkUserIfExist("xx@qq.com")).thenReturn(false);
        when(userService.saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }))).thenThrow(new PersistenceException("fake exception"));

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";

        mockMvc.perform(post("/user").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                     .content(requestBody)
                                     .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(500))
               .andExpect(jsonPath("$.errMsg").value("server error"))
               .andDo(print());

        verify(userService).checkUserIfExist(anyString());
        verify(userService).saveUser(argThat(new ArgumentMatcher<UserVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof UserVO;
            }
        }));
    }

    @Test
    public void getUserInfo_Success() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.getUserInfo(anyInt())).thenReturn(userInfoVO);

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(200))
               .andExpect(jsonPath("$.errMsg").value("request success")).andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).getUserInfo(anyInt());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void getUserInfo_ParamIllegalOrNotExists() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.getUserInfo(anyInt())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(jsonPath("$.errCode").value(400))
               .andExpect(jsonPath("$.errMsg").value("request illegal, param illegal or lack of necessary param"))
               .andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).getUserInfo(anyInt());
    }

    @Test
    public void getUserInfo_ThrowIllegalArgumentException() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.getUserInfo(anyInt())).thenThrow(IllegalArgumentException.class);

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(400))
               .andExpect(jsonPath("$.errMsg").value("request illegal, param illegal or lack of necessary param")).andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).getUserInfo(anyInt());
    }

    @Test
    public void getUserInfo_ThrowException() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        //noinspection unchecked
        when(userService.getUserInfo(anyInt())).thenThrow(Exception.class);

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(500))
               .andExpect(jsonPath("$.errMsg").value("server error")).andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).getUserInfo(anyInt());
    }

    @Test
    public void updateUserInfo_Suceess() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }))).thenReturn(userInfoVO);

        String requestBody = "{\"displayName\":\"xxx@qq.com\", \"age\":\"20\", \"gender\":\"F\"}";
        mockMvc.perform(put("/user/30").header("X-Token", "token")
                                       .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                       .content(requestBody)
                                       .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(200))
               .andExpect(jsonPath("$.errMsg").value("request success")).andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }));
    }

    @Test
    public void updateUserInfo_ParamIllegalOrNotExists() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }))).thenReturn(userInfoVO);

        String requestBody = "{\"displayName\":\"xxx@qq.com\", \"age\":\"160\", \"gender\":\"F\"}";
        mockMvc.perform(put("/user/30").header("X-Token", "token")
                                       .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                       .content(requestBody)
                                       .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(400))
               .andExpect(jsonPath("$.errMsg").value("request illegal, param illegal or lack of necessary param"))
               .andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService, never()).updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void updateUserInfo_ThrowException() throws Exception {

        when(tokenService.checkTokenIfExist(anyString())).thenReturn("30");
        when(userService.updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }))).thenThrow(Exception.class);

        String requestBody = "{\"displayName\":\"xxx@qq.com\", \"age\":\"120\", \"gender\":\"F\"}";
        mockMvc.perform(put("/user/30").header("X-Token", "token")
                                       .contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                       .content(requestBody)
                                       .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(500))
               .andExpect(jsonPath("$.errMsg").value("server error")).andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
        verify(userService).updateUserInfo(anyInt(), argThat(new ArgumentMatcher<SimpleUserInfoVO>() {
            @Override
            public boolean matches(Object o) {
                return o instanceof SimpleUserInfoVO;
            }
        }));
    }

    @Test
    public void authorizationInterceptor_NoPermission() throws Exception {
        when(tokenService.checkTokenIfExist(anyString())).thenReturn("15");

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(403))
               .andExpect(jsonPath("$.errMsg").value("no permission to request information"))
               .andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
    }

    @Test
    public void authorizationInterceptor_TokenNotExist() throws Exception {
        when(tokenService.checkTokenIfExist(anyString())).thenReturn("15");

        mockMvc.perform(get("/user/30"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(401))
               .andExpect(jsonPath("$.errMsg").value("token not exists or illegal"))
               .andDo(print());

        verify(tokenService, never()).checkTokenIfExist(anyString());
    }

    @Test
    public void authorizationInterceptor_TokenIllegal() throws Exception {
        when(tokenService.checkTokenIfExist(anyString())).thenReturn(null);

        mockMvc.perform(get("/user/30").header("X-Token", "token"))
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(401))
               .andExpect(jsonPath("$.errMsg").value("token not exists or illegal"))
               .andDo(print());

        verify(tokenService).checkTokenIfExist(anyString());
    }
}
