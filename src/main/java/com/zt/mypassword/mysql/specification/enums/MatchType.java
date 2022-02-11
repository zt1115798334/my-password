package com.zt.mypassword.mysql.specification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MatchType {
    /**
     * 等于
     */
    EQUAL("等于", "equal"),

    /**
     * 模糊查询
     */
    LIKE("模糊查询", "like"),

    /**
     * 为空
     */
    IS_NULL("为空", "isNull"),

    /**
     * 两个时间中间
     */
    BETWEEN_TIME("两个时间中间", "betweenTime"),

    /**
     * 在值之内
     */
    In("在值之内", "in"),

    /**
     * 在值之间
     */
    BETWEEN("在值之间", "between"),

    /**
     * 大于
     */
    GT("大于", "gt"),

    /**
     * 大于等于
     */
    GE("大于等于", "ge"),

    /**
     * 小于
     */
    LT("小于", "lt"),

    /**
     * 小于等于
     */
    LE("小于等于", "le");

    private final String method;
    private final String desc;

}
