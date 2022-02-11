package com.zt.mypassword.enums;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/23 11:37
 * description:
 */
@Getter
@AllArgsConstructor
public enum Sex {
    MAN("男"), WOMAN("女"), UNKNOWN("未知");

    private final String text;


    private static final Map<String, Sex> enumMap = Maps.newHashMap();

    static {
        for (Sex type : Sex.values()) {
            enumMap.put(type.getText(), type);
        }
    }

    public static Sex getEnum(String text) {
        return enumMap.get(text);
    }
}
