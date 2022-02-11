package com.zt.mypassword.mysql.specification;

import com.google.common.collect.Iterables;
import com.zt.mypassword.mysql.specification.enums.BooleanOperator;
import com.zt.mypassword.mysql.specification.enums.MatchType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SpecificationUtils<T> {
    private Specification<T> spec;

    private Method specMethod;

    interface SpecComparable<T> extends Comparable<String>, Expression<String> {
        default void toString(T t) {
            log.info("t:{}", t);
        }
    }

    public SpecificationUtils(BooleanOperator operator) {
        try {
            Specification<T> specification = (root, query, builder) -> null;
            this.spec = Specification.where(specification);
            this.specMethod = this.spec.getClass().getMethod(operator.getCode(), Specification.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 执行and 或者or方法
     *
     * @param specification specification
     */
    @SuppressWarnings("unchecked")
    private void invoke(Specification<T> specification) {
        try {
            this.spec = (Specification<T>) this.specMethod.invoke(this.spec, specification);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 去重
     *
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> distinct() {
        Specification<T> specification = (root, query, builder) -> {
            query.distinct(true);
            return null;
        };
        this.invoke(specification);
        return this;
    }

    /**
     * like
     *
     * @param key   字段名
     * @param value 字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> like(String key, String value) {
        if (this.check(MatchType.LIKE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.like(root.get(key), this.getLikePattern(value));
            this.invoke(specification);
        }
        return this;
    }

    /**
     * like
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> like(String outer, String inside, String value) {
        if (this.check(MatchType.LIKE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.like(root.get(outer).get(inside), this.getLikePattern(value));
            this.invoke(specification);
        }
        return this;
    }

    /**
     * like
     *
     * @param key1  字段
     * @param key2  字段
     * @param key3  字段
     * @param value 字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> like(String key1, String key2, String key3, String value) {
        if (this.check(MatchType.LIKE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.like(root.get(key1).get(key2).get(key3), this.getLikePattern(value));
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not like
     *
     * @param key   字段名
     * @param value 字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notLike(String key, String value) {
        if (this.check(MatchType.LIKE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.like(root.get(key), this.getLikePattern(value)).not();
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not like
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notLike(String outer, String inside, String value) {
        if (this.check(MatchType.LIKE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.like(root.get(outer).get(inside), this.getLikePattern(value)).not();
            this.invoke(specification);
        }
        return this;
    }

    /**
     * equal
     *
     * @param key   字段名
     * @param value 字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> equal(String key, Object value) {
        if (this.check(MatchType.EQUAL, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.equal(root.get(key), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * equal
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> equal(String outer, String inside, Object value) {
        if (this.check(MatchType.EQUAL, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.equal(root.get(outer).get(inside), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not equal
     *
     * @param key   字段名
     * @param value 字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notEqual(String key, Object value) {
        if (this.check(MatchType.EQUAL, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.equal(root.get(key), value).not();
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not equal
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  字段值
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notEqual(String outer, String inside, Object value) {
        if (this.check(MatchType.EQUAL, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.equal(root.get(outer).get(inside), value).not();
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 为空 is null
     *
     * @param key 字段名
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> isNull(String key) {
        Specification<T> specification = (root, query, builder) -> builder.isNull(root.get(key));
        this.invoke(specification);
        return this;
    }

    /**
     * 为空 is null
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> isNull(String outer, String inside) {
        Specification<T> specification = (root, query, builder) -> builder.isNull(root.get(outer).get(inside));
        this.invoke(specification);
        return this;
    }

    /**
     * 不为空 not null
     *
     * @param key 字段名
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notNull(String key) {
        Specification<T> specification = (root, query, builder) -> builder.isNull(root.get(key)).not();
        this.invoke(specification);
        return this;
    }

    /**
     * 不为空 not null
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notNull(String outer, String inside) {
        Specification<T> specification = (root, query, builder) -> builder.isNull(root.get(outer).get(inside)).not();
        this.invoke(specification);
        return this;
    }

    /**
     * in
     *
     * @param key    字段名
     * @param values 值 (可传入一个List类型，或可变长参数)
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> in(String key, Object... values) {
        if (this.check(MatchType.In, Arrays.stream(values).toArray())) {
            Specification<T> specification;
            if (values[0] instanceof List) {
                specification = (root, query, builder) -> builder.in(root.get(key)).value(values[0]);
            } else {
                specification = (root, query, builder) -> builder.in(root.get(key)).value(Arrays.asList(values));
            }
            this.invoke(specification);
        }
        return this;
    }

    /**
     * in
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param values 值 (可传入一个List类型，或可变长参数)
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> in(String outer, String inside, Object... values) {
        if (this.check(MatchType.In, values)) {
            Specification<T> specification;
            if (values[0] instanceof List) {
                specification = (root, query, builder) -> builder.in(root.get(outer).get(inside)).value(values[0]);
            } else {
                specification = (root, query, builder) -> builder.in(root.get(outer).get(inside)).value(Arrays.asList(values));
            }
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not in
     *
     * @param key    字段名
     * @param values 值 (可传入一个List类型，或可变长参数)
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notIn(String key, Object... values) {
        if (this.check(MatchType.In, Arrays.stream(values).toArray())) {
            Specification<T> specification;
            if (values[0] instanceof List) {
                specification = (root, query, builder) -> builder.in(root.get(key)).value(values[0]).not();
            } else {
                specification = (root, query, builder) -> builder.in(root.get(key)).value(Arrays.asList(values)).not();
            }
            this.invoke(specification);
        }
        return this;
    }

    /**
     * not in
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param values 值 (可传入一个List类型，或可变长参数)
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> notIn(String outer, String inside, Object... values) {
        if (this.check(MatchType.In, values)) {
            Specification<T> specification;
            if (values[0] instanceof List) {
                specification = (root, query, builder) -> builder.in(root.get(outer).get(inside)).value(values[0]).not();
            } else {
                specification = (root, query, builder) -> builder.in(root.get(outer).get(inside)).value(Arrays.asList(values)).not();
            }
            this.invoke(specification);
        }
        return this;
    }

    /**
     * between (闭区间,值参数不分大小)
     *
     * @param key   字段名
     * @param upper 值 类型必须实现Comparable接口
     * @param lower 值 类型必须实现Comparable接口
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> between(String key, SpecComparable<T> upper, SpecComparable<T> lower) {
        if (this.check(MatchType.BETWEEN, new Object[]{upper, lower})) {
            Specification<T> specification = (root, query, builder) -> builder.between(root.get(key), upper, lower);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * between (闭区间,值参数不分大小)
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param upper  值 类型必须实现Comparable接口
     * @param lower  值 类型必须实现Comparable接口
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> between(String outer, String inside, SpecComparable<T> upper, SpecComparable<T> lower) {
        if (this.check(MatchType.BETWEEN, new Object[]{upper, lower})) {
            Specification<T> specification = (root, query, builder) -> builder.between(root.get(outer).get(inside), upper, lower);
            this.invoke(specification);
        }
        return this;
    }


    /**
     * 大于 (greater than)
     *
     * @param key   字段名
     * @param value 值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> gt(String key, Number value) {
        if (this.check(MatchType.GT, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.gt((root.get(key)), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于 (greater than)
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> gt(String outer, String inside, Number value) {
        if (this.check(MatchType.GT, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.gt(root.get(outer).get(inside), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于等于（greater than or equal）
     *
     * @param key   字段名
     * @param value 值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> ge(String key, Number value) {
        if (this.check(MatchType.GE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.ge(root.get(key), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于等于（great than or equal）
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> ge(String outer, String inside, Number value) {
        if (this.check(MatchType.GE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.ge(root.get(outer).get(inside), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 小于 (less than)
     *
     * @param key   字段名
     * @param value 值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> lt(String key, Number value) {
        if (this.check(MatchType.LT, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.lt(root.get(key), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 小于(less than)
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> lt(String outer, String inside, Number value) {
        if (this.check(MatchType.LT, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.lt((root.get(outer).get(inside)), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于等于（less than or equal）
     *
     * @param key   字段名
     * @param value 值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> le(String key, Number value) {
        if (this.check(MatchType.LE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.le((root.get(key)), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于等于（less than or equal）
     *
     * @param key   字段名
     * @param value 值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> lessThanOrEqualTo(String key, LocalDateTime value) {
        if (this.check(MatchType.LE, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.lessThanOrEqualTo((root.get(key)), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 大于等于（less than or equal）
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param value  值 类型必须继承Number
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> le(String outer, String inside, Number value) {
        if (this.check(MatchType.LT, new Object[]{value})) {
            Specification<T> specification = (root, query, builder) -> builder.le((root.get(outer).get(inside)), value);
            this.invoke(specification);
        }
        return this;
    }

    /**
     * 查询两个时间内的内容(日期格式为yyyy-MM-dd),DO中类型必须为java.Util.Date (如果为Instant,请使用betweenInstant)
     *
     * @param key  字段名
     * @param time 时间数组，包含两个时间（开始时间、结束时间）
     * @return SpecificationUtils 返回time[0]00:00:00-time[1]23:59:59闭区间的内容
     */
    public SpecificationUtils<T> betweenDateStr(String key, List<String> time) {
        try {
            if (this.check(MatchType.BETWEEN_TIME, new Object[]{time})) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 转换时自动把时区时间转换成了世界时间
                Date start = new Date(format.parse(time.get(0)).toInstant().toEpochMilli());
                // 截止时间是结束那天的23时59分59秒
                Date end = new Date(format.parse(time.get(1)).toInstant().plus(Duration.ofHours(23).plusMinutes(59).plusSeconds(59)).toEpochMilli());
                Specification<T> specification = (root, query, builder) -> builder.between(root.get(key), start, end);
                this.invoke(specification);
            }
            return this;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询两个时间内的内容(日期格式为yyyy-MM-dd),DO中类型必须为java.Util.Date (如果为Instant,请使用betweenInstant)
     *
     * @param key  字段名
     * @param time 时间数组，包含两个时间（开始时间、结束时间）
     * @return SpecificationUtils 返回time[0]00:00:00-time[1]23:59:59闭区间的内容
     */
    public SpecificationUtils<T> betweenLocalDateTime(String key, List<LocalDateTime> time) {
        Iterables.removeIf(time, Objects::isNull);
        if (this.check(MatchType.BETWEEN_TIME, new Object[]{time}) && !time.isEmpty()) {
            // 转换时自动把时区时间转换成了世界时间
            LocalDateTime start = time.get(0);
            // 截止时间是结束那天的23时59分59秒
            LocalDateTime end = time.get(1);
            Specification<T> specification = (root, query, builder) -> builder.between(root.get(key), start, end);
            this.invoke(specification);
        }
        return this;


    }

    /**
     * 查询两个时间内的内容(日期格式为yyyy-MM-dd),DO中类型必须为java.Util.Date (如果为Instant,请使用betweenInstant)
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param time   时间数组，包含两个时间（开始时间、结束时间）
     * @return SpecificationUtils 返回time[0]00:00:00-time[1]23:59:59闭区间的内容
     */
    public SpecificationUtils<T> betweenDate(String outer, String inside, List<String> time) {
        try {
            if (this.check(MatchType.BETWEEN_TIME, new Object[]{time})) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 转换时自动把时区时间转换成了世界时间
                Date start = new Date(format.parse(time.get(0)).toInstant().toEpochMilli());
                // 截止时间是结束那天的23时59分59秒
                Date end = new Date(format.parse(time.get(1)).toInstant().plus(Duration.ofHours(23).plusMinutes(59).plusSeconds(59)).toEpochMilli());
                Specification<T> specification = (root, query, builder) -> builder.between(root.get(outer).get(inside), start, end);
                this.invoke(specification);
            }
            return this;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询两个时间内的内容(日期格式为yyyy-MM-dd),DO中类型必须为Instant (如果Date,请使用betweenDate)
     *
     * @param key  字段名
     * @param time 时间数组，包含两个时间（开始时间、结束时间）
     * @return SpecificationUtils 返回time[0]00:00:00-time[1]23:59:59闭区间的内容
     */
    public SpecificationUtils<T> betweenInstant(String key, List<String> time) {
        try {
            if (this.check(MatchType.BETWEEN_TIME, new Object[]{time})) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 转换时自动把时区时间转换成了世界时间
                Instant start = format.parse(time.get(0)).toInstant();
                // 截止时间是结束那天的23时59分59秒
                Instant end = format.parse(time.get(1)).toInstant().plus(Duration.ofHours(23).plusMinutes(59).plusSeconds(59));
                Specification<T> specification = (root, query, builder) -> builder.between(root.get(key), start, end);
                this.invoke(specification);
            }
            return this;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    /**
     * 查询两个时间内的内容(日期格式为yyyy-MM-dd),DO中类型必须为Instant (Date,请使用betweenDate)
     *
     * @param outer  外层字段名
     * @param inside 内层字段名
     * @param time   时间数组，包含两个时间（开始时间、结束时间）
     * @return SpecificationUtils 返回time[0]00:00:00-time[1]23:59:59闭区间的内容
     */
    public SpecificationUtils<T> betweenInstant(String outer, String inside, List<String> time) {
        try {
            if (this.check(MatchType.BETWEEN_TIME, new Object[]{time})) {
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                // 转换时自动把时区时间转换成了世界时间
                Instant start = format.parse(time.get(0)).toInstant();
                // 截止时间是结束那天的23时59分59秒
                Instant end = format.parse(time.get(1)).toInstant().plus(Duration.ofHours(23).plusMinutes(59).plusSeconds(59));
                Specification<T> specification = (root, query, builder) -> builder.between(root.get(outer).get(inside), start, end);
                this.invoke(specification);
            }
            return this;
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * 切换里面的内容为or
     *
     * @param specification 查询条件
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> or(Specification<T> specification) {
        this.spec = this.spec.or(specification);
        return this;
    }

    /**
     * 切换里面的内容为and
     *
     * @param specification 查询条件
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> and(Specification<T> specification) {
        this.spec = this.spec.and(specification);
        return this;
    }

    /**
     * 有选择的执行and
     *
     * @param condition     是否执行specification
     * @param specification 查询条件
     * @return SpecificationUtils
     */
//    public SpecificationUtils<T> or(Boolean condition, Specification<T> specification) {
//        if (condition) {
//            this.spec = this.spec.or(specification);
//        }
//        return this;
//    }

    /**
     * 有选择的执行or
     *
     * @param condition     是否执行specification
     * @param specification 查询条件
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> and(Boolean condition, Specification<T> specification) {
        if (condition) {
            this.spec = this.spec.and(specification);
        }
        return this;
    }

    /**
     * 切换后面内容为or
     *
     * @return SpecificationUtils
     */
//    public SpecificationUtils<T> or() {
//        try {
//            this.specMethod = this.spec.getClass().getMethod("or", Specification.class);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        }
//        return this;
//    }

    /**
     * 切换后面的内容为and
     *
     * @return SpecificationUtils
     */
    public SpecificationUtils<T> and() {
        try {
            this.specMethod = this.spec.getClass().getMethod("and", Specification.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return this;
    }


    private String getLikePattern(String content) {
        return "%" + content.trim() + "%";
    }

    /**
     * 校验
     *
     * @param operator 操作符
     * @param values   值
     * @return 校验是否为空，操作符为ISNULL时不做校验
     */
    private Boolean check(MatchType operator, Object[] values) {
        switch (operator) {
            case LIKE:
            case EQUAL:
                return ObjectUtils.notEqual(values[0], null);
            case GT:
            case GE:
            case LT:
            case LE:
            case BETWEEN_TIME:
                return null != values[0];
            case BETWEEN:
                if (values != null && values.length >= 2) {
                    return StringUtils.isNotBlank(values[0].toString()) && StringUtils.isNotBlank(values[1].toString());
                } else {
                    return false;
                }
            case In:
                return ArrayUtils.isNotEmpty(values) && null != values[0];
            case IS_NULL:
            default:
                break;
        }
        return true;
    }

    public Specification<T> build() {
        Specification<T> tempSpec = this.spec;
        Specification<T> specification = (root, query, builder) -> null;
        this.spec = Specification.where(specification);
        return tempSpec;
    }
}
