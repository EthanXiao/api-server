/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.model;

import java.util.Date;

import com.google.common.base.MoreObjects;

public class User {

    /**
     * primary key
     */
    private int userId;

    /**
     * unique key
     */
    private String email;

    private String password;

    /**
     * salt value
     */
    private String secretKey;

    private Date createdTime;

    private Date updateTime;

    private Date lastLogin;

    private int status;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(Date lastLogin) {
        this.lastLogin = lastLogin;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("userId", userId)
                          .add("email", email)
                          .add("password", password)
                          .add("secretKey", secretKey)
                          .add("createdTime", createdTime)
                          .add("updateTime", updateTime)
                          .add("lastLogin", lastLogin)
                          .add("status", status)
                          .toString();
    }
}
