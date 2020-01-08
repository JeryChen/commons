package com.ars.commons.utils;

import com.ars.commons.dto.UserDTO;

/**
 * 〈一句话介绍功能〉<br>
 * 用户信息本地线程存储器
 *
 * @author jierui on 2019-12-20.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class UserHolder {

    private static final ThreadLocal<UserDTO> USER_THREAD_LOCAL = new ThreadLocal<>();

    /**
     * 获取本地缓存用户信息
     *
     * @return 用户信息
     */
    public static UserDTO get() {
        return USER_THREAD_LOCAL.get();
    }

    /**
     * 添加本地缓存用户信息
     *
     * @param userDTO 用户信息
     */
    public static void add(UserDTO userDTO) {
        USER_THREAD_LOCAL.set(userDTO);
    }

    /**
     * 移除本地缓存用户信息
     */
    public static void remove() {
        USER_THREAD_LOCAL.remove();
    }

}
