package com.futurist_labs.android.base_library.utils;

import java.util.List;

/**
 * Created by Galeen on 10/4/2018.
 */
public class ListUtils {

    public static boolean isEmpty(List list){
        return list == null || list.size() == 0;
    }

    public static <T> boolean isEmpty(T[] list){
        return list == null || list.length == 0;
    }
}
