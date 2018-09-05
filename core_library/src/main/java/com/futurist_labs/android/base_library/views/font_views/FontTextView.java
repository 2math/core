package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.util.AttributeSet;

import com.futurist_labs.android.base_library.R;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontTextView extends android.support.v7.widget.AppCompatTextView {
    FontHelper fontHelper;

    public FontTextView(Context context) {
        super(context);
        init(context, null);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        fontHelper = new FontHelper(this, new FontHelper.StyleAttributes(R.styleable.FontTextView, R.styleable.FontTextView_tv_font,R.styleable.FontTextView_TvType));
        fontHelper.init(context, attrs);
    }

    public void setViewFont(FontHelper.FontType type){
        fontHelper.setViewFont(type);
    }
}
