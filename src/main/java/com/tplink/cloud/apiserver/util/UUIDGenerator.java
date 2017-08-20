/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.util;

import sun.misc.BASE64Encoder;

import java.util.UUID;

/**
 * generate uuid
 */
public final class UUIDGenerator {


    private UUIDGenerator() {}

    public static String generateRandomToken() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    public static String generateEncodingSecretKey() {
        String encodingAesKey = UUID.randomUUID().toString().replace("-", "");
        BASE64Encoder base64Encoder = new BASE64Encoder();
        String encodingSecretKey = base64Encoder.encode(encodingAesKey.getBytes());
        return encodingSecretKey.substring(0, encodingSecretKey.length() - 1);
    }

    public static void main(String[] args) {
        System.out.println(generateRandomToken());
    }
}
