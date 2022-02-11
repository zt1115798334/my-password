package com.zt.mypassword.controller;

import com.alibaba.fastjson.JSONObject;
import com.zt.mypassword.aop.SaveLog;
import com.zt.mypassword.base.user.CurrentUser;
import com.zt.mypassword.service.InheritService;
import com.zt.mypassword.shiro.token.PasswordToken;
import com.zt.mypassword.utils.NetworkUtil;
import com.zt.mypassword.utils.RequestResponseUtil;
import com.zt.mypassword.base.controller.BaseResultMessage;
import com.zt.mypassword.base.controller.ResultMessage;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/13 17:33
 * description:
 */
@Api(tags = "登录")
@Validated
@AllArgsConstructor
@RestController
@RequestMapping("api/login")
public class LoginController extends BaseResultMessage implements CurrentUser {

    private final InheritService inheritService;

    @ApiOperation(value = "登录")
    @PostMapping(value = "login", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "登录")
    public ResultMessage login(HttpServletRequest request,
                               @NotBlank(message = "账户不能为空") @RequestParam String username,
                               @NotBlank(message = "密码不能为空") @RequestParam String password,
                               @RequestParam(defaultValue = "") String verificationCode) {
        try {
            String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
            PasswordToken token = new PasswordToken(username, password);
            String accessToken = inheritService.login(token, verificationCode, ip);
            JSONObject result = new JSONObject();
            result.put("accessToken", accessToken);
            return success( result);
        } catch (Exception e) {
            return failure(e.getMessage());
        }
    }

    @ApiOperation(value = "发送手机验证码执行登录操作")
    @PostMapping(value = "sendVerificationCodeByLogin")
    @SaveLog(desc = "发送手机验证码执行登录操作")
    public ResultMessage sendVerificationCodeByLogin(HttpServletRequest request,
                                                     @RequestParam @NotBlank(message = "账户不能为空") String username) {
        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        inheritService.sendVerificationCodeByLogin(username, ip);
        return success("发送成功");
    }

    @ApiOperation(value = "注销")
    @PostMapping(value = "logout", produces = MediaType.APPLICATION_JSON_VALUE)
    @SaveLog(desc = "注销")
    public ResultMessage logout(HttpServletRequest request) {
        String ip = NetworkUtil.getLocalIp(RequestResponseUtil.getRequest(request));
        inheritService.logout(getCurrentUserId(), ip);
        return success();
    }
}
