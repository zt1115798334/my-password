package com.zt.mypassword.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang tong
 * date: 2018/8/20 15:31
 * description:
 */
@Slf4j
public class NetworkUtil {

    /**
     * 从Request对象中获得客户端IP，处理了HTTP代理服务器和Nginx的反向代理截取了ip
     *
     * @param request req
     * @return ip
     */
    public static String getLocalIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (isNotIP(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (isNotIP(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (isNotIP(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (isNotIP(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.equals("0:0:0:0:0:0:0:1")) {
            return "127.0.0.1";
        }
        return ip;
    }

    private static boolean isNotIP(String ip) {
        return ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip);
    }
}
