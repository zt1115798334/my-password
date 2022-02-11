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

    /**
     * 将127.0.0.1形式的IP地址转换成十进制整数，这里没有进行任何错误处理
     *
     * @param strIp ip
     * @return Long
     */
    public static Long ipToLong(String strIp) {
        long[] ip = new long[4];
        //先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        //将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址
     *
     * @param longIp ip
     * @return String
     */
    public static String longToIP(long longIp) {
        //直接右移24位
        return (longIp >>> 24) +
                "." +
                //将高8位置0，然后右移16位
                ((longIp & 0x00FFFFFF) >>> 16) +
                "." +
                //将高16位置0，然后右移8位
                ((longIp & 0x0000FFFF) >>> 8) +
                "." +
                //将高24位置0
                (longIp & 0x000000FF);
    }


    public static void main(String[] args) {
        String ipStr = "192.168.0.1";
        long longIp = NetworkUtil.ipToLong(ipStr);
        System.out.println("192.168.0.1 的整数形式为：" + longIp);
        System.out.println("整数" + longIp + "转化成字符串IP地址："
                + NetworkUtil.longToIP(longIp));
        //ip地址转化成二进制形式输出
        System.out.println("192.168.0.1 的二进制形式为：" + Long.toBinaryString(longIp));

    }
}
