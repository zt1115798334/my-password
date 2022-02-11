package com.zt.mypassword.utils;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.awt.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/11/17 13:43
 * description:
 */
public class MStringUtils {
    /**
     * 请求参数格式转换 aop 接口使用
     *
     * @param parameterNames  parameterNames
     * @param parameterValues parameterValues
     * @return String
     */
    public static String parseParams(String[] parameterNames, Object[] parameterValues) {
        StringBuilder stringBuffer = new StringBuilder();
        int length = parameterNames.length;
        for (int i = 0; i < length; i++) {
            String parameterName = Optional.ofNullable(parameterNames[i]).orElse("unknown");
            Object parameterValueObj = Optional.ofNullable(parameterValues[i]).orElse("unknown");
            Class<?> parameterValueClazz = parameterValueObj.getClass();
            String parameterValue;
            if (parameterValueClazz.isPrimitive() ||
                    parameterValueClazz == String.class) {
                parameterValue = parameterValueObj.toString();
            } else if (parameterValueObj instanceof MultipartFile) {
                parameterValue = "MultipartFile";
            } else if (parameterValueObj instanceof MultipartFile[]) {
                parameterValue = "MultipartFile";
            } else if (parameterValueObj instanceof Serializable) {
                parameterValue = JSON.toJSONString(parameterValueObj);
            } else {
                parameterValue = parameterValueObj.toString();
            }
            stringBuffer.append(parameterName).append(":").append(parameterValue).append(" ");
        }
        return stringBuffer.toString();
    }

    /**
     * 将中文符号转换为英文符号 并且清除空字符串
     *
     * @param text text
     * @return String
     */
    public static String stringToDBCClearBlank(String text) {
        return Optional.ofNullable(text)
                .map(MStringUtils::toDBC)
                .map(str -> Arrays.stream(str.split(",")).distinct().filter(StringUtils::isNotBlank).collect(Collectors.joining(","))) //清空 空字符串
                .orElse("");
    }

    /**
     * 转半角的函数(DBC case)
     * 任意字符串
     * 半角字符串
     * 全角空格为12288，半角空格为32
     * 其他字符半角(33-126)与全角(65281-65374)的对应关系是：均相差65248
     *
     * @param input input
     * @return String
     */
    public static String toDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375) {
                c[i] = (char) (c[i] - 65248);
            }
        }
        return new String(c);
    }


    public static String splicingSmsCodeText(String code) {
        return "验证码为：" + code + "，十分钟内有效！";
    }

}
