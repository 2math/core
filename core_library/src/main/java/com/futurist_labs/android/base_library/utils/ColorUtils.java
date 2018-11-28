package com.futurist_labs.android.base_library.utils;

import android.graphics.Color;

/**
 * Created by Galeen on 11/28/2018.
 */
public class ColorUtils {
    private static final String TAG = "ColorUtils";

    public static Integer parseColorOrNull(String colorHex) {
        if (colorHex == null || colorHex.length() == 0) {
            return null;
        }

        if (!colorHex.startsWith("#")) {
            colorHex = "#" + colorHex;
        }

        try {
            return Color.parseColor(colorHex);
        } catch (IllegalArgumentException ex) {
            LogUtils.d(TAG, "Invalid color:" + colorHex);
            return null;
        }
    }

    public static int parseColor(String colorHex) {
        return parseColor(colorHex,Color.BLACK);
    }

    public static int parseColor(String colorHex, int fallbackColor) {
        Integer col = parseColorOrNull(colorHex);
        return col == null ? fallbackColor : col;
    }
}
