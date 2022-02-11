package com.zt.mypassword.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/12/21 13:55
 * description: 状态码
 */
@AllArgsConstructor
@Getter
public enum SystemStatusCode {

    SUCCESS(0, "success", "成功"),
    FAILED(1, "failed", "失败"),

    USER_NORMAL(1000, "userNormal", "用户状态:正常"),
    USER_FROZEN(1001, "userFrozen", "用户状态:冻结"),
    USER_DELETE(1003, "userDelete", "用户状态:已删除"),
    USER_NOT_FOUND(1004, "userNotFound", "用户状态:未找到"),
    USER_TYPE_ADMIN(10001, "admin", "用户状态:管理员"),
    USER_TYPE_ORDINARY(10002, "ordinary", "用户状态:普通用户"),

    ES_PARAMS_VALIDATION_FAILED(4000, "paramsValidationFailed", "参数异常"),
    ES_ANALYTICAL_ANOMALY(4001, "esAnalyticalAnomaly", "es结果分析异常"),
    ES_RESPONSE_TIMEOUT(5000, "esResponseTimeout", "es接口响应超时"),

    JWT_ISSUED_SUCCESS(2000, "jwtIssuedSuccess", "jwt 签发成功"),
    JWT_ISSUED_FAILED(2001, "jwtIssuedFailed", "jwt 签发失败"),

    JWT_NEW(3000, "jwtNew", "accessToken过期，refreshToken未过期，返回新的jwt"),
    JWT_ACCESS_TOKEN_EXPIRE(30001, "jwtAccessTokenExpire", "accessToken过期"),
    JWT_ACCESS_TOKEN_ERROR(30002, "jwtAccessTokenError", "accessToken错误"),

    JWT_EXPIRE(3001, "jwtExpire", "jwt 已过期,通知客户端重新登录"),
    JWT_ERROR(3002, "jwtError", "jwt 认证失败无效,包括用户认证失败或者jwt令牌错误无效或者用户未登录"),
    JWT_NOT_FOUND(3003, "jwtNotFound", "jwt未找到"),

    PARAMS_VALIDATION_FAILED(4000, "paramsValidationFailed", "参数异常"),


    SC_UNAUTHORIZED(401, "unauthorized", "无权限访问"),
    SC_NOT_FOUND(401, "notFound", "没有找到页面"),
    SC_BAD_REQUEST(400, "badRequest", "参数解析失败"),
    SC_METHOD_NOT_ALLOWED(405, "methodNotAllowed", "不支持当前请求方"),
    SC_UNSUPPORTED_MEDIA_TYPE(415, "unsupportedMediaType", "不支持当前媒体类型"),
    SC_INTERNAL_SERVER_ERROR(500, "internalServerError", "系统错误");

    private final Integer code;
    private final String name;
    private final String msg;
}
