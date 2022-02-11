package com.zt.mypassword.utils;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zt.mypassword.utils.FileUtils.handleFileName;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/10/20 15:53
 * description:
 */
@Slf4j
public class HttpServletUtils {

    public static void modificationResponse(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String userAgent = request.getHeader("USER-AGENT");
        log.info("userAgent==:" + userAgent);
        fileName = handleFileName(userAgent, fileName);
        response.reset(); // reset the response
        response.setContentType("application/octet-stream");//告诉浏览器输出内容为流
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
    }
}
