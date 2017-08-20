/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.net>
 * Created: 17/07/25
 */

package com.tplink.cloud.apiserver.mapper;

import com.tplink.cloud.apiserver.dto.SimpleUserInfoVO;
import com.tplink.cloud.apiserver.model.UserInfo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Map;

public interface UserInfoMapper {

    @Insert("INSERT INTO xiaoxin_user_info(user_id,display_name,email,gender,age,created_time,update_time,status) "
                    + "VALUES (#{userId}, #{displayName}, #{email}, #{gender}, #{age} , NOW(), NOW(), "
                    + "#{status})")
    @Options(useGeneratedKeys = true, keyProperty = "infoId")
    void saveUserInfo(UserInfo userInfo);

    @Update("<script>"
                    + "UPDATE xiaoxin_user_info SET "
                    + "<if test=\"displayName != null\">"
                    + "display_name = #{displayName},"
                    + "</if>"
                    + "<if test=\"gender != null\">"
                    + "gender = #{gender},"
                    + "</if>"
                    + "<if test=\"age != null\">"
                    + "age = #{age}"
                    + "</if>"
                    + "WHERE user_id = #{userId}"
                    + "</script>")
    void updateUserInfo(SimpleUserInfoVO simpleUserInfoVO);

    @Select("SELECT * FROM xiaoxin_user_info WHERE user_id = #{userId}")
    UserInfo getUserInfo(int userId);
}
