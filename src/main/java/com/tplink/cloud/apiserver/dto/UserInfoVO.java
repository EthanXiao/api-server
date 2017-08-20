/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/31
 */

package com.tplink.cloud.apiserver.dto;

import java.io.Serializable;

import com.google.common.base.MoreObjects;

public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = -5959128951480761219L;

    private int userId;

    private String displayName;

    private String email;

    private String gender;

    private int age;

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

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).add("userId", userId)
                          .add("displayName", displayName)
                          .add("email", email)
                          .add("gender", gender)
                          .add("age", age).toString();
    }
}
