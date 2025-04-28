package io.github.lumine1909.blocktuner.util;

import java.lang.reflect.Field;

public class ReflectionUtil {

    @SuppressWarnings("unchecked")
    public static <T> T accessField(Class<?> clazz, String fieldName, Object instance) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void modifyField(Class<?> clazz, String fieldName, Object instance, Object value) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
