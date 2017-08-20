/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/02
 */

package com.tplink.cloud.apiserver.dto;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

public class TokenVO implements Serializable {

    private static final long serialVersionUID = -8874959949030402496L;

    private int userId;

    private String token;

    private long expiresIn;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("userId", userId)
                          .add("token", token)
                          .add("expiresIn", expiresIn)
                          .toString();
    }
}
