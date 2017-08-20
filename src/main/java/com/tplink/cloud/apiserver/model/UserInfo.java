/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.model;

import java.util.Date;

import com.google.common.base.MoreObjects;

public class UserInfo {

    /**
     * primary key
     */
    private int infoId;

    private int userId;

    private String displayName;

    private String email;

    private String gender;

    private int age;

    private Date createdTime;

    private Date updateTime;

    private int status;

    public int getInfoId() {
        return infoId;
    }

    public void setInfoId(int infoId) {
        this.infoId = infoId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Date getCreatedTime() {
        return createdTime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("infoId", infoId)
                          .add("userId", userId)
                          .add("displayName", displayName)
                          .add("email", email)
                          .add("gender", gender)
                          .add("age", age)
                          .add("createdTime", createdTime)
                          .add("updateTime", updateTime)
                          .add("status", status)
                          .toString();
    }
}
