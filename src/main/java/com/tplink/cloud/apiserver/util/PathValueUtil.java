/*
 * Copyright (c) 2017, TP-Link Co.,Ltd.
 * Author:  xiaoxin <xiaoxin@tp-link.com.cn>
 * Created: 2017-08-18
 */

package com.tplink.cloud.apiserver.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

/**
 * match @PathVariable param from url
 */
public class PathValueUtil {

    public static String getPathValue(Method method, HttpServletRequest request){
        RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
        Annotation[][] annotations = method.getParameterAnnotations();
        String value = null;
        for(Annotation[] annotation : annotations){
            for (Annotation annotation1 : annotation){
                if (annotation1 instanceof PathVariable){
                    value = ((PathVariable)annotation1).value();
                }
            }
        }

        String[] requestUrl = requestMapping.value();

        String[] s = requestUrl[0].split("/");
        int index = 0;
        for (int i = 0; i < s.length; i++){
            if(s[i].contains(value))
                index = i;
        }
        return request.getRequestURI().split("/")[index];
    }

}
