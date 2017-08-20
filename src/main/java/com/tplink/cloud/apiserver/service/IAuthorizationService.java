/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.service;

public interface IAuthorizationService {

    /**
     * Authentication operation
     * return userId
     * return null if user no exist or password no right
     */
    Integer attemptAuthentication(String email, String password);
}
