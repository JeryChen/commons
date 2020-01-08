package com.ars.commons.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 〈一句话介绍功能〉<br>
 * 用户信息
 *
 * @author jierui on 2019-12-20.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = -3905127584339692987L;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;
}
