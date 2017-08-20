/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/07
 */

package com.tplink.cloud.apiserver.service;

import static org.junit.Assert.*;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Strings;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.tplink.cloud.apiserver.dto.TokenVO;
import com.tplink.cloud.apiserver.service.impl.TokenServiceImpl;
import com.tplink.cloud.apiserver.dao.RedisDao;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({ "classpath:unit-test.xml" })
public class TokenServiceTest {

    @InjectMocks
    private TokenServiceImpl tokenService;

    @Mock
    private RedisDao redisDao;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        ReflectionTestUtils.setField(tokenService, "expiredIn", 7200);
        ReflectionTestUtils.setField(tokenService, "tokenSize", 10);
    }

    @Test
    public void saveToken_Success() {

        Set<String> set1 = new HashSet<>();
        for (int i = 0; i < 5; i++) {
            set1.add(String.valueOf(i));
        }
        Set<String> set2 = new HashSet<>();
        for (int i = 0; i < 11; i++) {
            set2.add(String.valueOf(i));
        }
        when(redisDao.zadd(anyString(), anyDouble(), anyString())).thenReturn(new Long(1));
        when(redisDao.zrangBySore(anyString(), anyDouble(), anyDouble())).thenReturn(set1).thenReturn(set2);
        when(redisDao.zremrangeByRank(anyString(), anyLong(), anyLong())).thenReturn(1L);

        TokenVO tokenVO = tokenService.saveToken(1234);
        verify(redisDao).zadd(anyString(), anyDouble(), anyString());
        verify(redisDao).zrangBySore(anyString(), anyDouble(), anyDouble());
        assertNotNull(tokenVO);

        TokenVO tokenVO2 = tokenService.saveToken(1234);
        verify(redisDao, times(2)).zadd(anyString(), anyDouble(), anyString());
        verify(redisDao).zremrangeByRank(anyString(), anyLong(), anyLong());
        verify(redisDao, times(2)).zrangBySore(anyString(), anyDouble(), anyDouble());
        assertNotNull(tokenVO2);
    }

    @Test
    public void checkTokenIfExist() {

        when(redisDao.get(anyString())).thenReturn("1");

        String userId = tokenService.checkTokenIfExist("1234");
        assertTrue(!Strings.isNullOrEmpty(userId));
        verify(redisDao).get(anyString());
    }

    @Test
    public void delToken() {

        when(redisDao.zrem(anyString(), anyString())).thenReturn(1L);
        when(redisDao.del(anyString())).thenReturn(1L);

        boolean result = tokenService.delToken("1234");

        assertTrue(result);

        verify(redisDao).get(anyString());
        verify(redisDao).zrem(anyString(), anyString());
        verify(redisDao).del(anyString());
    }
}
