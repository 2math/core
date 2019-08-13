package com.futurist_labs.android.base_library.utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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


    private static final List<String> excludeList = new ArrayList<>(Arrays.asList("ß", "à", "á", "â", "ã", "ä", "å", "æ", "ç",
            "è", "é", "ê", "ë", "ì", "í", "î", "ï", "ñ", "ò", "ó", "ô", "õ", "ö", "ø", "ù", "ú", "û", "ü", "ý", "ÿ", "œ", "š", "ž", "ƒ", "ē", "’"));
    private static final List<String> includeList = new ArrayList<>(Arrays.asList("s", "a", "a", "a", "a", "a", "a", "ae", "c"
            , "e", "e", "e", "e", "i", "i", "i", "i", "n", "o", "o", "o", "o", "o", "o", "u", "u", "u", "u", "y", "y", "oe", "s", "s", "f", "e", "'"));

    /**
     * This will replace special latin characters as "à" to "a"
     * @param textToEscape the text will be converted to LowerCase first
     * @return escaped text - LowerCased
     */
    public static String escapeFromFrench(String textToEscape) {
        if (textToEscape == null) return null;
        textToEscape = textToEscape.toLowerCase();
        //for each forbidden char
        for (int i = excludeList.size() - 1; i >= 0; i--) {
            textToEscape = textToEscape.replace(excludeList.get(i), includeList.get(i));
        }
        return textToEscape;
    }
}
