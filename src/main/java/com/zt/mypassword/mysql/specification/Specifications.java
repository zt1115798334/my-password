package com.zt.mypassword.mysql.specification;

import com.zt.mypassword.mysql.specification.enums.BooleanOperator;

public class Specifications {
    public static <T> SpecificationUtils<T> and() {
        return new SpecificationUtils<>(BooleanOperator.AND);
    }

    public static <T> SpecificationUtils<T> or() {
        return new SpecificationUtils<>(BooleanOperator.OR);
    }
}
