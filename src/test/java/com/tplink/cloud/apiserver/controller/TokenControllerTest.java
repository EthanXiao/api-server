/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/07
 */

package com.tplink.cloud.apiserver.controller;

import java.sql.SQLException;

import org.apache.ibatis.exceptions.PersistenceException;
import org.h2.jdbc.JdbcSQLException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisDataException;

import com.tplink.cloud.apiserver.dto.TokenVO;
import com.tplink.cloud.apiserver.service.IAuthorizationService;
import com.tplink.cloud.apiserver.service.ITokenService;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringJUnit4ClassRunner.class)
public class TokenControllerTest extends ControllerTestBase {

    private MockMvc mockMvc;

    @InjectMocks
    private TokenController tokenController;

    @Mock
    private ITokenService tokenService;

    @Mock
    private IAuthorizationService authorizationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        this.mockMvc =
                MockMvcBuilders.standaloneSetup(tokenController).build();
    }

    @Test
    public void getToken_Success() throws Exception {

        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken("token");
        tokenVO.setUserId(1);
        tokenVO.setExpiresIn(7200);
        when(authorizationService.attemptAuthentication(anyString(), anyString())).thenReturn(1);
        when(tokenService.saveToken(anyInt())).thenReturn(tokenVO);

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";
        mockMvc.perform(post("/token").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                      .content(requestBody)
                                      .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(200))
               .andExpect(jsonPath("$.errMsg").value("request success"));
    }

    @Test
    public void getToken_PasswordIllegal() throws Exception {

        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken("token");
        tokenVO.setUserId(1);
        tokenVO.setExpiresIn(7200);
        when(authorizationService.attemptAuthentication(anyString(), anyString())).thenReturn(1);
        when(tokenService.saveToken(anyInt())).thenReturn(tokenVO);

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"1234\"}";
        mockMvc.perform(post("/token").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                      .content(requestBody)
                                      .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(400))
               .andExpect(jsonPath("$.errMsg").value("request illegal, param illegal or lack of necessary param"));
    }

    @Test
    public void getToken_UsernameOrPasswordMistake() throws Exception {
        TokenVO tokenVO = new TokenVO();
        tokenVO.setToken("token");
        tokenVO.setUserId(1);
        tokenVO.setExpiresIn(7200);
        when(authorizationService.attemptAuthentication(anyString(), anyString())).thenReturn(null);
        when(tokenService.saveToken(anyInt())).thenReturn(tokenVO);

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";
        mockMvc.perform(post("/token").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                      .content(requestBody)
                                      .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(401))
               .andExpect(jsonPath("$.errMsg").value("username or password mistake"));
    }

    @Test
    public void getToken_throwException() throws Exception {
        when(authorizationService.attemptAuthentication(anyString(), anyString()))
                .thenThrow(new PersistenceException("fake exception"));

        String requestBody = "{\"email\":xx@qq.com, \"password\":\"xx1234\"}";
        mockMvc.perform(post("/token").contentType(MediaType.parseMediaType("application/json;charset=UTF-8"))
                                        .content(requestBody)
                                        .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(500))
               .andExpect(jsonPath("$.errMsg").value("server error"));
    }

    @Test
    public void delToken_Success() throws Exception {

        when(tokenService.delToken(anyString())).thenReturn(true);

        mockMvc.perform(delete("/token/cbc878032d854fa5ae8451eace2aceca")
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(200))
               .andExpect(jsonPath("$.errMsg").value("delete token success"));
    }

    @Test
    public void delToken_TOKEN_NOT_EXISTS_OR_ILLEGAL() throws Exception {
        when(tokenService.delToken(anyString())).thenThrow(JedisDataException.class);

        mockMvc.perform(delete("/token/cbc878032d854fa5ae8451eace2aceca")
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(401))
               .andExpect(jsonPath("$.errMsg").value("token not exists or illegal"));
    }

    @Test
    public void delToken_ThrowException() throws Exception {
        when(tokenService.delToken(anyString())).thenThrow(new JedisConnectionException("fake exception"));

        mockMvc.perform(delete("/token/cbc878032d854fa5ae8451eace2aceca")
                                .accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
               .andDo(print())
               .andExpect(content().contentTypeCompatibleWith("application/json"))
               .andExpect(jsonPath("$.errCode").value(401))
               .andExpect(jsonPath("$.errMsg").value("token not exists or illegal"));
    }
}
