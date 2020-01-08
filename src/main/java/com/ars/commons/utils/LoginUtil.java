package com.ars.commons.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * 〈一句话介绍功能〉<br>
 * 登录处理工具类
 *
 * @author jierui on 18/1/18.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class LoginUtil {

    /**
     * 平台登录用户cookie的name
     */
    public static final String COOKIE_USER_KEY = "LOGIN_USER_KEY";

    /**
     * 平台登录用户request的name
     */
    public static final String REQUEST_ATTRIBUTE_USER_LEY = "REQUEST_USER_KEY";

    /**
     * cookie有效时长
     */
    public static final Integer COOKIE_TIME_OUT = 60 * 30;

    /**
     * 用于用户登录拼接redis中的key
     */
    public static final String USER_LOGIN = "USER_LOGIN_";

    /**
     * 从cookie中获取用户缓存在redis中的key
     * @param request 请求信息
     * @return 用户key
     */
    public static String getUserKey(HttpServletRequest request) {
        String userKey = null;
        Cookie[] cookies = request.getCookies();
        if (null != cookies && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (COOKIE_USER_KEY.equals(cookie.getName())) {
                    userKey = cookie.getValue();
                    break;
                }
            }
        }
        return userKey;
    }

    /**
     * 初始构造cookie
     * @param userKey 用户key
     * @return cookie
     */
    public static Cookie generateCookie(String userKey) {
        Cookie cookie = new Cookie(COOKIE_USER_KEY, userKey);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_TIME_OUT);
        return cookie;
    }

    /**
     * 初始构造用户缓存在redis中的key
     * @param userKey 用户key
     * @return redis中的key
     */
    public static String generateRedisKey(String userKey) {
        return USER_LOGIN + userKey;
    }

    /**
     * 清除cookie
     * @param request 请求信息
     * @return cookie
     */
    public static Cookie expireCookie(HttpServletRequest request) {
        Cookie result = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            // 删除用户信息cookie
            for (Cookie cookie : cookies) {
                if (COOKIE_USER_KEY.equals(cookie.getName())) {
                    result = cookie;
                    result.setMaxAge(0);
                    break;
                }
            }
        }
        return result;
    }

}
