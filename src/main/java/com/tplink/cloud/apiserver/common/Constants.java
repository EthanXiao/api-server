/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/26
 */

package com.tplink.cloud.apiserver.common;

public class Constants {

    public static final String TOKEN_REQUEST_HEAD = "X-Token";

    public static final int DEFAULT_AGE = 0;

    public static final String DEFAULT_GENDER = "M";

    public final static String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

    public final static String REGEX_PASSWORD = "[a-z0-9A-Z~!@#$%^&*()_+|<>,.?/:;'\\\\[\\\\]{}\\\"]{6,32}";

    public final static int DEFAULT_DISPLAYNAME_SIZE = 32;

    public final static int DEFAULT_PASSWORD_SIZE = 32;

    public final static int MAX_AGE = 150;

    public final static int MIN_AGE = 0;
}
