package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.utils.SystemUtils;
import com.futurist_labs.android.base_library.utils.photo.HttpImageGetter;

import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatTextView;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontTextView extends AppCompatTextView {
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

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fontHelper.onDraw(canvas);
    }

    private void init(Context context, AttributeSet attrs) {
        fontHelper = new FontHelper(this, new FontHelper.StyleAttributes(R.styleable.FontTextView, R.styleable
                .FontTextView_tv_font, R.styleable.FontTextView_TvType, R.styleable.FontTextView_strike, R.styleable.FontTextView_strikeColor));
        fontHelper.init(context, attrs);
    }

    public void setViewFont(FontHelper.FontType type) {
        fontHelper.setViewFont(type);
    }

    public void setTextOrHide(String text) {
        if (text == null || text.isEmpty()) {
            setVisibility(GONE);
        } else {
            setText(text);
            setVisibility(VISIBLE);
        }
    }

    public boolean setHTMLTextOrHide(String text) {
        return loadHtml(text, 0);
    }

    public boolean setHTMLTextOrHide(String text, @DrawableRes int imagesPlaceholder) {
        return loadHtml(text, imagesPlaceholder);
    }

    private boolean loadHtml(String text, int imagesPlaceholder) {
        if (TextUtils.isEmpty(text)) {
            setVisibility(GONE);
            return false;
        } else {
            setVisibility(VISIBLE);
            setText(SystemUtils.parseHtml(text, imagesPlaceholder != 0 ?
                    new HttpImageGetter(this, imagesPlaceholder) : new HttpImageGetter(this)));
            return true;
        }
    }
}
