package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.google.android.material.textfield.TextInputLayout;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontTextInputLayout extends TextInputLayout {

    public FontTextInputLayout(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextInputLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    // TODO: 2/21/2019 set custom font from xml
    private void init(Context context, AttributeSet attrs) {
        //we need to do that with 100 mills delay, because setting the font for the editText inside overrides this font too. Now we have font for hints.
        postDelayed(new Runnable() {
            @Override
            public void run() {
                Typeface font = FontHelper.getTypeface(FontHelper.getBoldFont(),getContext());
                setTypeface(font);
            }
        },100);
    }
}
