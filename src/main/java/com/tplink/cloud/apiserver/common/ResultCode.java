/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.common;

public enum ResultCode {

    SUCCESS(200, "request success"),

    SUCCESS_DELETE_TOKEN(200, "delete token success"),

    PARAM_ILLEGAL_OR_NOT_EXISTS(400, "request illegal, param illegal or lack of necessary param"),

    TOKEN_NOT_EXISTS_OR_ILLEGAL(401, "token not exists or illegal"),

    USERNAME_OR_PASSWORD_MISTAKE(401, "username or password mistake"),

    NO_PERMISSION_TO_REQUEST(403, "no permission to request information"),

    USER_ALREADY_EXISTS(409, "user already exists"),

    SYSTEM_ERROR(500, "server error");

    public int getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }


    private int errCode;

    private String errMsg;


    ResultCode(int errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }
}
