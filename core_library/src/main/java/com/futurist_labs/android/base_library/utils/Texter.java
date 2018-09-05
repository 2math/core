package com.futurist_labs.android.base_library.utils;

/**
 * Created by Galeen on 8/23/2018.
 * Methods which can be called from unit tests
 * TextUtils is from package android.text
 */
public class Texter {
    // TODO: 8/23/2018 copy most methods from TextUtils
    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();
    }
}
