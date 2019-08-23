package com.futurist_labs.android.base_library.utils;

import java.lang.reflect.Field;

/**
 * Created by Galeen on 8/21/2019.
 */
public class CloneUtil {
    // TODO: 8/23/2019 check transient fields
    public static <T> T clone(T t) {
        Class<?> clazzRoot = t.getClass();

        Object newInstance = null;
        try {
            newInstance = clazzRoot.newInstance();
            Field[] fieldsClone = newInstance.getClass().getDeclaredFields();
            for (Field fieldClone : fieldsClone) {
                fieldClone.setAccessible(true);
                fieldClone.set(newInstance, getContent(t, fieldClone.getName()));
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
        return (T) newInstance;
    }

    private static Object getContent(Object aClass, String name) throws NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        Field declaredField = aClass.getClass().getDeclaredField(name);
        declaredField.setAccessible(true);
        return  declaredField.get(aClass);
    }
}
