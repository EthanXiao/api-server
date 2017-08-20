/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/31
 */

package com.tplink.cloud.apiserver.interceptor;

import com.tplink.cloud.apiserver.common.ApiResult;
import com.tplink.cloud.apiserver.common.Authorization;
import com.tplink.cloud.apiserver.common.Constants;
import com.tplink.cloud.apiserver.common.ResultCode;
import com.tplink.cloud.apiserver.service.ITokenService;
import com.tplink.cloud.apiserver.util.PathValueUtil;

import com.google.common.base.Strings;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Authentication interceptor
 * Handle methods which has authentication annotation
 */
public class AuthorizationInterceptor extends HandlerInterceptorAdapter {

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthorizationInterceptor.class);

    @Resource
    private ITokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        if (!(handler instanceof HandlerMethod)) {
            return super.preHandle(request, response, handler);
        }

        HandlerMethod handlerMethod = (HandlerMethod)handler;
        Method method = handlerMethod.getMethod();

        if(null == method.getAnnotation(Authorization.class)) {
            return super.preHandle(request, response, handler);
        }

        String token = request.getHeader(Constants.TOKEN_REQUEST_HEAD);
        ApiResult result = ApiResult.getDefaultInstance();
        Gson gson = new Gson();

        if(Strings.isNullOrEmpty(token)){
            LOGGER.warn("token is null.");
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.setResultCode(ResultCode.TOKEN_NOT_EXISTS_OR_ILLEGAL);
            response.getOutputStream().write(gson.toJson(result).getBytes());
            return false;
        }

        String id = tokenService.checkTokenIfExist(token);

        if(Strings.isNullOrEmpty(id)){
            LOGGER.warn("token not exists. token;[{}]", token);
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            result.setResultCode(ResultCode.TOKEN_NOT_EXISTS_OR_ILLEGAL);
            response.getOutputStream().write(gson.toJson(result).getBytes());
            return false;
        }

        String userId = PathValueUtil.getPathValue(method, request);
        if (id.equals(userId)) {
            return super.preHandle(request, response, handler);
        } else {
            LOGGER.warn("no permission to request information. userId;[{}], id:[{}]",id, request.getParameter("id"));
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/json");
            result.setResultCode(ResultCode.NO_PERMISSION_TO_REQUEST);
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().write(gson.toJson(result).getBytes());
            return false;
        }

    }
}
