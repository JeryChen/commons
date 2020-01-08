package com.ars.commons.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 〈一句话介绍功能〉<br>
 * 〈功能详细描述〉
 *
 * @author jierui on 2020-01-08.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldCheck {

    /**
     * 字段名
     * @return
     */
    String name() default "";

    /**
     * 不能为Null
     * @return
     */
    boolean notNull() default false;

    /**
     * 不是为Null或者为空字符串，空数组，空集合
     * @return
     */
    boolean notEmpty() default false;

    /**
     * 字段最大长度限制
     * 用于字符串
     * @return
     */
    int maxLength() default -1;

    /**
     * 字段最小长度限制
     * 用于字符串
     * @return
     */
    int minLength() default -1;

    /**
     * 正则表达式验证
     * 用于字符串
     * @return
     */
    String regex() default "";
}
