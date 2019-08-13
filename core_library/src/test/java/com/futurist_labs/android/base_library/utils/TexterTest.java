package com.futurist_labs.android.base_library.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Galeen on 5/21/2019.
 */
public class TexterTest {

    @Test
    public void isEmpty() {
        assertTrue(Texter.isEmpty(null));
        assertTrue(Texter.isEmpty(""));
        assertTrue(!Texter.isEmpty("asda"));
    }

    @Test
    public void escapeFromFrench() {
        String text = "ßàá";
        String textUpper = "SÀÁ";
        String escaped = "saa";
        assertNull(Texter.escapeFromFrench(null));
        assertNotNull(Texter.escapeFromFrench(text));
        assertEquals("", Texter.escapeFromFrench(""));
        assertEquals(escaped, Texter.escapeFromFrench(text));
        assertEquals(escaped, Texter.escapeFromFrench(textUpper));
    }
}