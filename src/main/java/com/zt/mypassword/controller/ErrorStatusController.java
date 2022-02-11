package com.zt.mypassword.controller;

import com.zt.mypassword.base.controller.BaseResultMessage;
import com.zt.mypassword.base.controller.ResultMessage;
import com.zt.mypassword.enums.SystemStatusCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/9/13 16:17
 * description:
 */
@RestController
@RequestMapping("api/error")
public class ErrorStatusController extends BaseResultMessage {

    /**
     * 401页面
     */
    @GetMapping(value = "401")
    public ResultMessage error_401() {
        SystemStatusCode scUnauthorized = SystemStatusCode.SC_UNAUTHORIZED;
        return new ResultMessage().error(scUnauthorized.getCode(), scUnauthorized.getMsg());
    }

    /**
     * 404页面
     */
    @GetMapping(value = "404")
    public ResultMessage error_404() {
        SystemStatusCode scNotFound = SystemStatusCode.SC_NOT_FOUND;
        return new ResultMessage().error(scNotFound.getCode(), scNotFound.getMsg());
    }

    /**
     * 500页面
     */
    @GetMapping(value = "500")
    public ResultMessage error_500() {
        SystemStatusCode scInternalServerError = SystemStatusCode.SC_INTERNAL_SERVER_ERROR;
        return new ResultMessage().error(scInternalServerError.getCode(), scInternalServerError.getMsg());
    }
}
