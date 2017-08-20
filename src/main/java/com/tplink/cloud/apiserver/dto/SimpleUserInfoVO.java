/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/08/13
 */

package com.tplink.cloud.apiserver.dto;

import java.io.Serializable;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import com.tplink.cloud.apiserver.common.Constants;
import com.tplink.cloud.apiserver.common.Gender;

/**
 * for update userInfo
 */
public class SimpleUserInfoVO implements Serializable {

    private static final long serialVersionUID = -6592498724183069454L;

    private int userId;

    private String displayName;

    private String age;

    private String gender;

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public boolean isValid() {
        if (Strings.isNullOrEmpty(displayName) || displayName.length() > Constants.DEFAULT_DISPLAYNAME_SIZE) {
            return false;
        }
        if (Strings.isNullOrEmpty(age) || Integer.valueOf(age) > Constants.MAX_AGE
                || Integer.valueOf(age) < Constants.MIN_AGE) {
            return false;
        }
        if (!(gender.equals(Gender.F.toString()) || gender.equals(Gender.M.toString()))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("userId", userId)
                          .add("displayName", displayName)
                          .add("age", age)
                          .add("gender", gender)
                          .toString();
    }
}
