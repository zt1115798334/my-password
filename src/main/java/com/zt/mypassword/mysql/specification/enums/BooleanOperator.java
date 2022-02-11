package com.zt.mypassword.mysql.specification.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BooleanOperator {
    /**
     * and
     */
    AND("and"),

    /**
     * or
     */
    OR("or");

    private final String code;

}
