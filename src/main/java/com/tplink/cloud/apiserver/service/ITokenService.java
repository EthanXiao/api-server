/*
/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service;

import com.tplink.cloud.apiserver.dto.TokenVO;

public interface ITokenService {

    /**
     * save token
     * save both token:id and id:(token1, token2..) in redis
     * @param userId
     * @return tokenVO
     */
    TokenVO saveToken(int userId);

    /**
     * check if token  exist,
     * return userId,
     * return Null if key not exists;
     */
    String checkTokenIfExist(String token);

    /**
     * delete token
     * remove both token:id and id:(token1, token2..) in redis
     * @param token
     * @return
     */
    boolean delToken(String token);
}
