package com.futurist_labs.android.base_library.utils.photo.image_loader;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.futurist_labs.android.base_library.utils.LogUtils;
import com.futurist_labs.android.base_library.utils.files.FileHelper;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * Created by Galeen on 20.11.2016 г..
 */
public class ImageTarget implements Target {
    private ImageView imageView;
    private int placeholder, error;
    private String id, dir;

    public ImageTarget(ImageView imageView, int placeholder, int error, String id, String dir) {
        this.imageView = imageView;
        this.placeholder = placeholder;
        this.error = error;
        this.id = id;
        this.dir = dir;
    }

    public ImageTarget(ImageView imageView, int placeholder) {
        this.imageView = imageView;
        this.placeholder = placeholder;
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        imageView.setImageBitmap(bitmap);
        imageView.clearAnimation();
        LogUtils.d("Image loaded");
        if (id != null && dir != null) {
            FileHelper.saveImageToFileAsync(bitmap,
                    0, 0, false, id, dir,
                    new FileHelper.Callback() {
                        @Override
                        public void onResult(String filePath) {
                        }
                    });
        }
    }

    @Override
    public void onBitmapFailed(Exception e,Drawable errorDrawable) {
        if (error != 0) imageView.setImageResource(error);
        imageView.clearAnimation();
        LogUtils.d("Image Failed");
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
        if (placeholder != 0) imageView.setImageResource(placeholder);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ImageTarget)) return false;

        ImageTarget that = (ImageTarget) o;

        return imageView != null ? imageView.equals(that.imageView) : that.imageView == null;

    }

    @Override
    public int hashCode() {
        return imageView != null ? imageView.hashCode() : 0;
    }

    public int getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(int placeholder) {
        this.placeholder = placeholder;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
