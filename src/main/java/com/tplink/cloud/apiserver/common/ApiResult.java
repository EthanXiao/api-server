/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.common;

import com.google.common.base.MoreObjects;

/**
 * result data format
 *
 *
 */
public class ApiResult {

    private int errCode;

    private String errMsg;

    private Object data;

    public static ApiResult getDefaultInstance() {
        return new ApiResult(null);
    }

    private ApiResult(Object data) {
        this.errCode = ResultCode.SUCCESS.getErrCode();
        this.errMsg = ResultCode.SUCCESS.getErrMsg();
        this.data = data;
    }

    public void setResultCode(ResultCode resultCode) {
        this.errCode = resultCode.getErrCode();
        this.errMsg = resultCode.getErrMsg();
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                          .add("errCode", errCode)
                          .add("errMsg", errMsg)
                          .add("data", data)
                          .toString();
    }
}
