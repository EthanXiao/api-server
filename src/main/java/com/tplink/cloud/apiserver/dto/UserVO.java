/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/26
 */

package com.tplink.cloud.apiserver.dto;

import java.io.Serializable;
import java.util.regex.Pattern;

import com.google.common.base.MoreObjects;
import com.google.common.base.Strings;

import com.tplink.cloud.apiserver.common.Constants;

/**
 *
 */
public class UserVO implements Serializable {

    private static final long serialVersionUID = 437313690046112504L;

    private String displayName;

    private String email;

    private String password;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("displayName", displayName)
                          .add("email", email)
                          .add("password", password).toString();
    }

    public boolean checkValid() {
        if (!Strings.isNullOrEmpty(displayName) && displayName.length() > Constants.DEFAULT_DISPLAYNAME_SIZE) {
            return false;
        }
        if (Strings.isNullOrEmpty(email) || !Pattern.matches(Constants.REGEX_EMAIL, email)) {
            return false;
        }
        if (Strings.isNullOrEmpty(password) || !Pattern.matches(Constants.REGEX_PASSWORD, password)
                || password.length() > Constants.DEFAULT_PASSWORD_SIZE) {
            return false;
        }
        return true;
    }
}
