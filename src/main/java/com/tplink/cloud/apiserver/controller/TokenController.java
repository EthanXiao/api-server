/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/24
 */

package com.tplink.cloud.apiserver.controller;

import javax.annotation.Resource;

import com.tplink.cloud.apiserver.common.ApiResult;
import com.tplink.cloud.apiserver.common.ResultCode;
import com.tplink.cloud.apiserver.dto.TokenVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.service.IAuthorizationService;
import com.tplink.cloud.apiserver.service.ITokenService;

import com.google.common.base.Preconditions;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class TokenController {

    private final static Logger LOGGER = org.slf4j.LoggerFactory.getLogger(TokenController.class);

    @Resource
    private ITokenService tokenService;

    @Resource
    private IAuthorizationService authorityService;

    @RequestMapping(value = "/token", method = RequestMethod.POST)
    public ResponseEntity<ApiResult> getToken(@RequestBody UserVO userVO) {
        ApiResult result = ApiResult.getDefaultInstance();
        LOGGER.debug("enter getToken method, username:[{}], password:[{}]", userVO.getEmail(), userVO.getPassword());

        try {
            //check param
            Preconditions.checkNotNull(userVO);

            if (!userVO.checkValid()) {
                LOGGER.warn("get token fail, userVO is invalid. userVO:[{}]", userVO);
                throw new IllegalArgumentException("get token fail, userVO is invalid");
            }

            //insert and get tokenVO
            Integer userId = authorityService.attemptAuthentication(userVO.getEmail(), userVO.getPassword());
            if (null != userId) {
                TokenVO tokenVO = tokenService.saveToken(userId);
                LOGGER.debug("get token success! userId:[{}], token:[{}]", userId, tokenVO);
                result.setData(tokenVO);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("get toke fail. username or password mistake.");
                result.setResultCode(ResultCode.USERNAME_OR_PASSWORD_MISTAKE);
                return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("user login fail, request param illegal. errMsg:[{}]", e.getMessage(), e);
            result.setResultCode(ResultCode.PARAM_ILLEGAL_OR_NOT_EXISTS);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("user login fail, system error. UserVO:[{}], errMsg:[{}]", userVO, e.getMessage(), e);
            result.setResultCode(ResultCode.SYSTEM_ERROR);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/token/{token}", method = RequestMethod.DELETE)
    public ResponseEntity<ApiResult> delToken(@PathVariable("token") String token) {
        ApiResult result = ApiResult.getDefaultInstance();
        LOGGER.debug("enter delete token method. token:[{}]", token);

        try {
            tokenService.delToken(token);
            LOGGER.debug("successful delete token");
            result.setResultCode(ResultCode.SUCCESS_DELETE_TOKEN);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e) {
            LOGGER.error("delete token fail, system error. token:[{}], errMsg:[{}]", token, e.getMessage(), e);
            result.setResultCode(ResultCode.TOKEN_NOT_EXISTS_OR_ILLEGAL);
            return new ResponseEntity<>(result, HttpStatus.UNAUTHORIZED);
        }
    }

}
