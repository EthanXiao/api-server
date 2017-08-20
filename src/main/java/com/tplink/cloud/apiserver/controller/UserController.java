/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/24
 */

package com.tplink.cloud.apiserver.controller;

import javax.annotation.Resource;

import com.google.common.base.Preconditions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.tplink.cloud.apiserver.common.ApiResult;
import com.tplink.cloud.apiserver.common.Authorization;
import com.tplink.cloud.apiserver.common.ResultCode;
import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.dto.UserInfoVO;
import com.tplink.cloud.apiserver.dto.UserVO;
import com.tplink.cloud.apiserver.exception.BeanUtilException;
import com.tplink.cloud.apiserver.service.IUserService;

@RestController
public class UserController {

    private final static Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Resource
    private IUserService userService;

    @RequestMapping(value = "/ping", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<String> ping() {
        return new ResponseEntity<>("success", HttpStatus.OK);
    }

    @RequestMapping(value = "/user", method = RequestMethod.POST, produces = "application/json")
    public ResponseEntity<ApiResult> register(
            @RequestBody UserVO userVO) {
        ApiResult result = ApiResult.getDefaultInstance();
        LOGGER.debug("enter register method, email:[{}], displayame:[{}]", userVO.getEmail(), userVO.getDisplayName());

        try {
            Preconditions.checkNotNull(userVO);
            if (!userVO.checkValid()) {
                throw new IllegalArgumentException("userVO is illegal");
            }

            // check if user account exist.
            if (userService.checkUserIfExist(userVO.getEmail())) {
                LOGGER.warn("user already exist! UserVO:[{}]", userVO);
                result.setResultCode(ResultCode.USER_ALREADY_EXISTS);
                return new ResponseEntity<>(result, HttpStatus.CONFLICT);
            }

            // create and return user info.
            UserInfoVO userInfoVO = userService.saveUser(userVO);
            if (userInfoVO != null) {
                LOGGER.debug("register user success! userInfo:[{}]", userInfoVO);
                result.setData(userInfoVO);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("system error, can't get userInfo. userVO ;[{}]", userVO);
                result.setResultCode(ResultCode.SYSTEM_ERROR);
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("user register fail, request param illegal. errMsg:[{}]", e.getMessage(), e);
            result.setResultCode(ResultCode.PARAM_ILLEGAL_OR_NOT_EXISTS);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (BeanUtilException e) {
            LOGGER.warn("user register fail, BizException. UserVO:[{}], errMsg:[{}]", userVO, e.getErrorMsg(), e);
            result.setResultCode(ResultCode.SYSTEM_ERROR);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e) {
            LOGGER.error("user register fail, system error. UserVO:[{}], errMsg:[{}]", userVO, e);
            result.setResultCode(ResultCode.SYSTEM_ERROR);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Authorization
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<ApiResult> getUserInfo(
            @PathVariable("id") Integer userId) {
        ApiResult result = ApiResult.getDefaultInstance();
        LOGGER.debug("enter getUserInfo method, userId:[{}]", userId);
        try {
            UserInfoVO userInfoVO = userService.getUserInfo(userId);
            if (null != userInfoVO) {
                LOGGER.debug("get userInfo success! userInfo:[{}]", userInfoVO);
                result.setData(userInfoVO);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("system error, can't get userInfo. userId ;[{}]", userId);
                result.setResultCode(ResultCode.SYSTEM_ERROR);
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("get user info fail, request param illegal. errMsg:[{}]", e.getMessage(), e);
            result.setResultCode(ResultCode.PARAM_ILLEGAL_OR_NOT_EXISTS);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("get user info fail, system error. UserId:[{}], errMsg:[{}]", userId, e.getMessage(), e);
            result.setResultCode(ResultCode.SYSTEM_ERROR);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Authorization
    @RequestMapping(value = "/user/{id}", method = RequestMethod.PUT)
    public ResponseEntity<ApiResult> updateUserInfo(
            @PathVariable("id") Integer userId, @RequestBody SimpleUserInfoVO simpleUserInfoVO) {
        ApiResult result = ApiResult.getDefaultInstance();
        LOGGER.debug("enter update UserInfo method, userId:[{}]", userId);

        try {
            //check data
            Preconditions.checkNotNull(simpleUserInfoVO);
            if (!simpleUserInfoVO.isValid()) {
                throw new IllegalArgumentException("userInfoVO is illegal");
            }

            UserInfoVO userInfoVO = userService.updateUserInfo(userId, simpleUserInfoVO);
            if (null != userInfoVO) {
                LOGGER.debug("update userInfo success! userInVO:[{}]", userInfoVO);
                result.setData(userInfoVO);
                return new ResponseEntity<>(result, HttpStatus.OK);
            } else {
                LOGGER.warn("system error, can't update userInfo. userId ;[{}], simpleUserInfoVO:[{}]", userId,
                            simpleUserInfoVO);
                result.setResultCode(ResultCode.SYSTEM_ERROR);
                return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (IllegalArgumentException e) {
            LOGGER.warn("update user info fail, request param illegal. errMsg:[{}]", e.getMessage(), e);
            result.setResultCode(ResultCode.PARAM_ILLEGAL_OR_NOT_EXISTS);
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            LOGGER.error("update user info fail, system error. userId:[{}], errMsg:[{}]", userId, e.getMessage(), e);
            result.setResultCode(ResultCode.SYSTEM_ERROR);
            return new ResponseEntity<>(result, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
