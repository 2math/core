package com.futurist_labs.android.base_library.utils.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Galeen on 2019-08-13.
 * Helper class to test an object for NULL/Empty fields.
 * This way you can easily test server response for mandatory fields that are null or if a field
 * is removed from the server response.
 *
 * Todo check for extra fields added form the server
 */
public class CoreAnnotations {

    /**
     * Fields with this annotation must be checked
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface MandatoryForTests {
    }
    /**
     * Fields with this annotation must not be checked
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ExcludeFromTests {
    }
    /**
     * Fields with this annotation will be checked if are primitive type and if is default value
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CheckDefaultPrimitiveInTests {
    }
    /**
     * String Fields with this annotation will be checked for empty string also String.length == 0
     */
    @Retention(RetentionPolicy.RUNTIME)
    public @interface CheckEmptyStringTests {
    }

    /**
     *
     * @param t Object to test
     * @param fieldsWithoutAnnotationAreMandatory if true any field that has no annotation @ExcludeFromTests
     *                                            will be treated as with  @MandatoryForTests, else only fields that has
     *                                            @MandatoryForTests annotation will be checked
     * @return Null if all is good , else will be a string where each line represents the field and msg if is NULL , 0 or Empty
     */
    public static <T> String getNullFields(T t, boolean fieldsWithoutAnnotationAreMandatory) {
        List<Field> fields = new ArrayList<>();
        Class clazz = t.getClass();
        while (clazz != null && clazz != Object.class) {
            fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass();
        }
        int size = fields.size();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; i++) {
            Field field = fields.get(i);
            if (!field.isAnnotationPresent(ExcludeFromTests.class) &&
                (fieldsWithoutAnnotationAreMandatory || field.isAnnotationPresent(MandatoryForTests.class))) {
                field.setAccessible(true);
                try {
                    if (field.get(t) == null) {
                        sb.append("\n").append(field.toGenericString()).append(" is NULL");
                    } else if (field.getType().isPrimitive()) {
                        //fields must have CheckDefaultPrimitiveInTests annotation to check if against default value, else is skipped
                        if (field.isAnnotationPresent(CheckDefaultPrimitiveInTests.class)) {
                            checkDefaultPrimitive(t, sb, field);
                        }
                    } else if (String.class.equals(field.getType()) && field.isAnnotationPresent(CheckEmptyStringTests.class)) {
                        String str = (String) field.get(t);
                        if (str.length() == 0) {
                            sb.append("\n").append(field.toGenericString()).append(" is empty");
                        }
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return sb.length() > 0 ? sb.toString() : null;
    }

    private static <T> void checkDefaultPrimitive(final T t, final StringBuilder sb, final Field field) throws IllegalAccessException {
        Object o = field.get(t);
        // any number type is fine.
        if (o instanceof Long) {
            if (field.getLong(t) == 0L) {
                sb.append("\n").append(field.toGenericString()).append(" is 0L");
            }
        } else if (o instanceof Double) {
            if (field.getDouble(t) == 0.0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0.0");
            }
        } else if (o instanceof Float) {
            if (field.getFloat(t) == 0.0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0.0f");
            }
        } else if (o instanceof Integer) {
            if (field.getInt(t) == 0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0");
            }
        } else if (o instanceof Byte) {
            if (field.getByte(t) == 0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0");
            }
        } else if (o instanceof Short) {
            if (field.getShort(t) == 0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0");
            }
        } else if (o instanceof Character) {
            if (field.getChar(t) == 0) {
                sb.append("\n").append(field.toGenericString()).append(" is 0");
            }
        } else if (o instanceof Boolean && !field.getBoolean(t)) {
            sb.append("\n").append(field.toGenericString()).append(" is FALSE");
        }
    }
}
