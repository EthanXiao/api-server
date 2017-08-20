/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.exception;

import com.tplink.cloud.apiserver.common.ResultCode;

public class BeanUtilException extends RuntimeException {

    private static final long serialVersionUID = -4837588054239794827L;

    private int errorCode;

    private String errorMsg;

    public BeanUtilException(ResultCode resultCode) {
        this.errorCode = resultCode.getErrCode();
        this.errorMsg = resultCode.getErrMsg();
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
