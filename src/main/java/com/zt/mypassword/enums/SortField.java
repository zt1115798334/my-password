package com.zt.mypassword.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/10/28 14:21
 * description:
 */
@Getter
@AllArgsConstructor
public enum SortField {
    wordFrequency("wordFrequency"),
    publishTime("publishTime"),
    authorPy("author.SPY");

    private final String name;
}
