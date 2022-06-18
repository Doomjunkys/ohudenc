package org.itkk.udf.dal.mybatis.plugin.pagequery;

import lombok.extern.slf4j.Slf4j;
import org.itkk.udf.core.exception.SystemRuntimeException;

import java.lang.reflect.Field;

/**
 * <p>
 * ClassName: ReflectHelper
 * </p>
 * <p>
 * Description: 反射工具类
 * </p>
 * <p>
 * Author: wangkang
 * </p>
 * <p>
 * Date: 2016年3月23日
 * </p>
 */
@Slf4j
public class ReflectHelper {

    /**
     * <p>
     * Description: 构造函数
     * </p>
     */
    private ReflectHelper() {

    }

    /**
     * <p>
     * Description: 根据字段名获得字段
     * </p>
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 字段
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                log.trace("ReflectHelper.getFieldByFieldName", e);
            }
        }
        return null;
    }

    /**
     * <p>
     * Description: 根据字段名获得值
     * </p>
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @return 值
     * @throws IllegalAccessException 异常
     */
    public static Object getValueByFieldName(Object obj, String fieldName) throws IllegalAccessException {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        if (field != null) {
            if (field.isAccessible()) {
                value = field.get(obj);
            } else {
                field.setAccessible(true);
                value = field.get(obj);
                field.setAccessible(false);
            }
        }
        return value;
    }

    /**
     * <p>
     * Description: 根据字段名设置字段值
     * </p>
     *
     * @param obj       对象
     * @param fieldName 字段名
     * @param value     值
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (field.isAccessible()) {
                field.set(obj, value);
            } else {
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(false);
            }
        } catch (SecurityException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            throw new SystemRuntimeException(e);
        }
    }

}
