package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TextInputEditText;
import android.text.InputType;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

import com.futurist_labs.android.base_library.R;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontTextInputEditTextView extends TextInputEditText {
    FontHelper fontHelper;

    public FontTextInputEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextInputEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextInputEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        fontHelper = new FontHelper(this,
                new FontHelper.StyleAttributes(R.styleable.FontTextInputEditTextView, R.styleable.FontTextInputEditTextView_tv_font,R.styleable.FontTextInputEditTextView_TvType));
        fontHelper.init(context, attrs);
    }

    public static void showHidePass(EditText etPassword, TextView tvShowPass){
        Typeface font = etPassword.getTypeface();
        if (etPassword.getInputType() == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            etPassword.setInputType(InputType.TYPE_CLASS_TEXT |
                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
            if(tvShowPass!=null) tvShowPass.setText(R.string.btn_show_password);
        } else {
            etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            if(tvShowPass!=null) tvShowPass.setText(R.string.btn_hide_password);
        }
        etPassword.setSelection(etPassword.getText().length());
        etPassword.setTypeface(font);
    }

    public String toText(){
        return getText().toString();
    }
}
