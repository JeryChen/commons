package com.ars.commons.utils;

import com.ars.commons.annotation.FieldCheck;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * 〈一句话介绍功能〉<br>
 * 字段检查工具
 *
 * @author jierui on 2020-01-08.
 * @see [相关类/方法]（可选）
 * @since [产品/模块版本] （可选）
 */
public class FieldCheckUtils {

    private static final String NOT_EMPTY = "[%s]不能为空";

    private static final String NOT_NUll = "[%s]不能为Null";

    private static final String MAX_LENGTH = "[%s]长度不能超过%d";

    private static final String REGEX = "[%s]格式不正确";

    /**
     * 字段校验
     * 只要发生字段校验失败，就抛出异常
     *
     * @param obj
     */
    public static void checkFailFast(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        if (declaredFields.length <= 0) {
            return;
        }
        for (Field field : declaredFields) {
            checkField(field, obj);
        }
    }

    /**
     * 集合字段校验
     * 只要发生字段校验失败，就抛出异常
     *
     * @param objects
     */
    public static void checkArrayFailFast(List<?> objects) {
        for (Object object : objects) {
            checkFailFast(object);
        }
    }

    /**
     * 字段校验
     * 所有字段校验完后，合并所有失败结果后抛出异常
     *
     * @param obj
     */
    public static void checkAll(Object obj) {
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        if (declaredFields.length <= 0) {
            return;
        }
        List<IllegalArgumentException> exceptionList = new ArrayList<IllegalArgumentException>();
        for (Field field : declaredFields) {
            try {
                checkField(field, obj);
            } catch (IllegalArgumentException e) {
                exceptionList.add(e);
            }
        }
        if (exceptionList.size() > 0) {
            throw new IllegalArgumentException(joinExceptionMessage(exceptionList));
        }
    }

    /**
     * 校验字段
     *
     * @param field
     * @param obj
     */
    private static void checkField(Field field, Object obj) {
        FieldCheck fieldCheck = field.getAnnotation(FieldCheck.class);
        if (fieldCheck == null) {
            return;
        }

        String fieldName = fieldCheck.name();
        if (StringUtils.isBlank(fieldName)) {
            fieldName = field.getName();
        }

        Object fieldValue = null;
        try {
            fieldValue = getValue(field, obj);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException("[" + fieldName + "]值获取异常", e);
        }

        //1、是否做notEmpty检查
        if (fieldCheck.notEmpty()) {
            notEmpty(fieldValue, fieldName);
            //2、是否做notNull检查
        } else if (fieldCheck.notNull()) {
            notNull(fieldValue, fieldName);
        }

        //3、是否做长度限制检查
        if (fieldCheck.maxLength() > 0) {
            maxLength(fieldValue, fieldName, fieldCheck.maxLength());
        }

        //4、是否做正则表达式检查
        if (fieldCheck.regex() != null && fieldCheck.regex().length() > 0) {
            regex(fieldValue, fieldName, fieldCheck.regex());
        }

    }

    /**
     * 非空检查
     *
     * @param fieldValue
     * @param fieldName
     */
    private static void notEmpty(Object fieldValue, String fieldName) {
        Assert.notNull(fieldValue, String.format(NOT_EMPTY, fieldName));

        if (fieldValue instanceof String) {
            Assert.hasText((String) fieldValue, String.format(NOT_EMPTY, fieldName));
        } else if (fieldValue instanceof Object[]) {
            Assert.notEmpty((Object[]) fieldValue, String.format(NOT_EMPTY, fieldName));
        } else if (fieldValue instanceof Collection) {
            Assert.notEmpty((Collection) fieldValue, String.format(NOT_EMPTY, fieldName));
        } else if (fieldValue instanceof Map) {
            Assert.notEmpty((Map) fieldValue, String.format(NOT_EMPTY, fieldName));
        }
    }

    /**
     * 非Null检查
     *
     * @param fieldValue
     * @param fieldName
     */
    private static void notNull(Object fieldValue, String fieldName) {
        Assert.notNull(fieldValue, String.format(NOT_NUll, fieldName));
    }

    /**
     * 最大长度校验
     *
     * @param fieldValue
     * @param fieldName
     * @param maxLength
     */
    private static void maxLength(Object fieldValue, String fieldName, int maxLength) {
        if (fieldValue != null && fieldValue instanceof CharSequence) {
            Assert.isTrue(((CharSequence) fieldValue).length() <= maxLength, String.format(MAX_LENGTH, fieldName, maxLength));
        }
    }

    /**
     * 正则表达式校验
     *
     * @param fieldValue
     * @param fieldName
     * @param regex
     */
    private static void regex(Object fieldValue, String fieldName, String regex) {
        if (fieldValue != null && fieldValue instanceof CharSequence) {
            boolean matched = Pattern.compile(regex).matcher((CharSequence) fieldValue).find();
            Assert.isTrue(matched, String.format(REGEX, fieldName));
        }
    }

    /**
     * 获取字段的值
     *
     * @param field
     * @param obj
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    private static Object getValue(Field field, Object obj)
            throws IllegalArgumentException, IllegalAccessException {
        field.setAccessible(true);
        return field.get(obj);
    }

    /**
     * 合并异常信息
     *
     * @param exceptionList
     * @return
     */
    private static String joinExceptionMessage(List<IllegalArgumentException> exceptionList) {
        StringBuilder exceptionMsg = new StringBuilder();
        for (IllegalArgumentException exception : exceptionList) {
            exceptionMsg.append(exception.getMessage()).append(";");
        }
        return exceptionMsg.toString();
    }
}
