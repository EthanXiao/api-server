/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.mapper;

import com.tplink.cloud.apiserver.model.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Select;

public interface UserMapper {

    @Insert("INSERT INTO xiaoxin_user(email, password, secret_key, created_time, update_time, last_login, status)"
                    + " VALUES (#{email}, #{password}, #{secretKey}, NOW(), NOW(), NOW(), 1)")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    void saveUser(User user);

    @Select("SELECT COUNT(*) FROM xiaoxin_user WHERE email = #{email}")
    boolean checkIfExist(@Param("email") String email);

    @Select("SELECT * FROM xiaoxin_user WHERE email = #{email} LIMIT 1")
    User getUserByEmail(String email);
}
