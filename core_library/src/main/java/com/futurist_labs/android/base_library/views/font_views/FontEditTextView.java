package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.util.AttributeSet;

import com.futurist_labs.android.base_library.R;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontEditTextView extends android.support.v7.widget.AppCompatEditText {
    FontHelper fontHelper;

    public FontEditTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontEditTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontEditTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        fontHelper = new FontHelper(this,
                new FontHelper.StyleAttributes(R.styleable.FontEditTextView, R.styleable.FontEditTextView_tv_font,R.styleable.FontEditTextView_TvType));
//                new FontHelper.StyleAttributes(R.styleable.GaleenEditTextView, R.styleable.GaleenEditTextView_tv_font_e,R.styleable.GaleenEditTextView_galeenTvType_e));
        fontHelper.init(context, attrs);
    }

    public String toText(){
        return getText().toString();
    }

    public void setTextOrHide(String text){
        if(text == null || text.isEmpty()){
            setVisibility(GONE);
        }else{
            setText(text);
            setVisibility(VISIBLE);
        }
    }
}
