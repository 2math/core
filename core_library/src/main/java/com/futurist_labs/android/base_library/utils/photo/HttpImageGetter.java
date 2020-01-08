package com.futurist_labs.android.base_library.utils.photo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import com.squareup.picasso.Target;

import androidx.annotation.DrawableRes;

/**
 * Created by Galeen on 12/9/2018.
 */
public class HttpImageGetter implements Html.ImageGetter {
    private @DrawableRes
    int placeholder;
    private TextView textView = null;
    private Context context;

    public HttpImageGetter(Context context) {
        this.context = context;
    }

    public HttpImageGetter(TextView textView) {
        this(textView.getContext());
        this.textView = textView;
    }

    public HttpImageGetter(TextView textView, int placeholder) {
        this(textView);
        this.placeholder = placeholder;
    }

    @Override
    public Drawable getDrawable(String source) {
        if (context == null) return null;
        BitmapDrawablePlaceHolder drawable = new BitmapDrawablePlaceHolder();
        RequestCreator rc = Picasso.with(context)
                .load(source);
        if (placeholder != 0) {
            rc.placeholder(placeholder);
        }
        rc.into(drawable);
        return drawable;
    }

    private class BitmapDrawablePlaceHolder extends BitmapDrawable implements Target {

        protected Drawable drawable;

        @Override
        public void draw(final Canvas canvas) {
            if (drawable != null) {
                drawable.draw(canvas);
            }
        }

        public void setDrawable(Drawable drawable) {
            this.drawable = drawable;
            int width = drawable.getIntrinsicWidth();
            int height = drawable.getIntrinsicHeight();
            drawable.setBounds(0, 0, width, height);
            setBounds(0, 0, width, height);
            if (textView != null) {
                textView.setText(textView.getText());
            }
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            setDrawable(new BitmapDrawable(context.getResources(), bitmap));
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {

        }

    }
}