package com.futurist_labs.android.base_library.utils.photo.image_loader;

import android.content.Context;
import android.widget.ImageView;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.io.File;

/**
 * Created by Galeen on 9/6/2018.
 */
public class ImageLoader {
    public static final String AVATAR_DIR = "avatar";
    public static final String IMAGES_DIR = "app_images";
    public static String IMAGES_URL = "";// set in MyApplication


    public static void loadImage(String imageId, ImageView iv, int placeholder) {
        if (iv != null && imageId != null) {
            loadImage(iv.getContext(), imageId, iv, IMAGES_DIR,
                    placeholder, null);
        }
    }

    public static void loadImage(String imageId, ImageView iv, int placeholder, ImageTarget target) {
        if (iv != null && imageId != null) {
            loadImage(iv.getContext(), imageId, iv, IMAGES_DIR,
                    placeholder, target);
        }
    }

    //
    private static void loadImage(Context ctx, String id, ImageTarget target) {
        Picasso.with(ctx).load(
                IMAGES_URL + id)
                .into(target);
    }

    protected static void loadImage(Context ctx, final String id, final ImageView iv, final String dir,
                                    final int placeholderRes, final ImageTarget target) {
        if (iv != null && id != null) {
            RequestCreator rc;
            File file = new File(new File(BaseLibraryConfiguration.getInstance().getApplication().getExternalFilesDir(null), dir), id);
            Picasso picasso = Picasso.with(ctx);
//            picasso.setLoggingEnabled(true);
            if (file.exists()) {
                rc = picasso.load(file);
                if (iv.getHeight() > 0) {
                    rc.resize(iv.getWidth(), iv.getHeight())
                            .centerInside();
                }
                if (placeholderRes != 0) {
                    rc.placeholder(placeholderRes);
                }
                rc.into(iv);
            } else {
                if (target == null) {
                    final ImageTarget imageTarget = new ImageTarget(iv, placeholderRes, placeholderRes, id, dir);
                    iv.setTag(imageTarget);
                    loadImage(ctx, id, imageTarget);
                } else {
                    loadImage(ctx, id, target);
                }
//                rc.into(target);
//                        new Target() {
//                    @Override
//                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                        if (iv != null) {
//                            iv.setImageBitmap(bitmap);
//                            LogUtils.d("Image loaded");
//                        }else{
//                            LogUtils.d("Image not loaded - iv.get() = null");
//                        }
//
//                        FileHelper.saveImageToFileAsync(bitmap,
//                                0, 0, false, id, dir,
//                                new FileHelper.Callback() {
//                                    @Override
//                                    public void onResult(String filePath) {
////                                        if (iv.get() != null) {
////                                            Picasso.with(iv.get().getContext())
////                                                    .load(filePath)
////                                                    .fit()
////                                                    .into(iv.get());
////                                        }
//                                    }
//                                });
//                    }
//
//                    @Override
//                    public void onBitmapFailed(Drawable errorDrawable) {
//                        if (iv != null && placeholderRes != 0) {
//                            iv.setImageResource(placeholderRes);
//                        }
//                        LogUtils.d("Image Failed");
//                    }
//
//                    @Override
//                    public void onPrepareLoad(Drawable placeHolderDrawable) {
//                        if (iv != null && placeholderRes != 0) {
//                            iv.setImageResource(placeholderRes);
//                        }
//                    }
//                });
            }
        }
    }
}
