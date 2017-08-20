/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.util;

import com.tplink.cloud.apiserver.common.ResultCode;
import com.tplink.cloud.apiserver.exception.BeanUtilException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Bean copy util
 */
public final class BeanUtil {

    private final static Logger LOGGER = LoggerFactory.getLogger(BeanUtil.class);

    private final static Map<String, BeanCopier> copierMap = new ConcurrentHashMap<>();

    /**
     * copy properties from source bean to target
     * @param src
     * @param target
     * @param <T>
     * @param <V>
     * @return
     */
    public static <T, V> V copyFrom(T src, Class<V> target) {
        StringBuilder sb = new StringBuilder();
        sb.append(src.getClass().getName()).append("-").append(target.getName());
        BeanCopier beanCopier = null;

        if (!copierMap.containsKey(sb.toString())) {
            beanCopier = BeanCopier.create(src.getClass(), target, false);
            copierMap.put(sb.toString(), beanCopier);
        }
        beanCopier = copierMap.get(sb.toString());
        V result = null;

        try {
            result = target.newInstance();
            beanCopier.copy(src, result, null);
            return result;
        } catch (InstantiationException | IllegalAccessException e) {
            LOGGER.warn("Bean copy fail. errMsg:[{}]", e);
            throw new BeanUtilException(ResultCode.SYSTEM_ERROR);
        }
    }


}
