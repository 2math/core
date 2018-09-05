package com.futurist_labs.android.base_library.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class GaleenCustomShapedImageView extends android.support.v7.widget.AppCompatImageView {
    public final static int CIRCLE = 0;
    public final static int TRIANGLE = 1;
    public final static int HEXAGON = 2;
    public final static int OVAL = 3;
    public final static int OCTAGON = 4;
    public final static int ROUNDED_CORNERS = 5;

    private int type = CIRCLE;
    private float ovalWideIndex = 1.4f;
    private float octaCornerIndex = 0.1f;
    private float cornerX = 20, cornerY = 20;

    public GaleenCustomShapedImageView(Context context) {
        super(context);
    }

    public GaleenCustomShapedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GaleenCustomShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private Bitmap b, bitmap, roundBitmap;
    private Drawable drawable;

    @Override
    protected void onDraw(Canvas canvas) {

        drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }
        b = ((BitmapDrawable) drawable).getBitmap();
        bitmap = b.copy(Config.ARGB_8888, true);

        int w = getWidth(), h = getHeight();

//        Bitmap roundBitmap;
        switch (type) {
            case TRIANGLE:
                roundBitmap = getTriangleCroppedBitmap(bitmap, w);
                break;
            case HEXAGON:
                roundBitmap = getHexagonCroppedBitmap(bitmap, w);
                break;
            case OVAL:
                roundBitmap = getOvalCroppedBitmap(bitmap, w, ovalWideIndex);
                break;
            case ROUNDED_CORNERS:
                roundBitmap =
//                        getRoundedCornerBitmap(bitmap, (int) cornerX);
                        getRoundedCornersCroppedBitmap(bitmap, w, h, cornerX, cornerY);
                break;
            case OCTAGON:
                roundBitmap = getOctagonCroppedBitmap(bitmap, w, octaCornerIndex);
                break;
            // CIRCLE
            default:
                if (h > w)
                    roundBitmap = getRoundedCroppedBitmap(bitmap, w);
                else
                    roundBitmap = getRoundedCroppedBitmap(bitmap, h);
                break;
        }

        canvas.drawBitmap(roundBitmap, 0, 0, null);

        bitmap.recycle();
//        b.recycle();
    }

    public static Bitmap getRoundedCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f, finalBitmap.getHeight() / 2 + 0.7f, finalBitmap.getWidth() / 2 + 0.1f,
                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getRoundedCornersCroppedBitmap(Bitmap bitmap, int ivWidth, int ivHeight, float cornerX, float cornerY) {
//        Bitmap finalBitmap;
        int bmWidth = bitmap.getWidth();
        int bmHeight = bitmap.getHeight();
        if (bmWidth > ivWidth || bmHeight > ivHeight) {
            float scale = ivHeight > ivWidth ? ivWidth / ivHeight : ivHeight / ivWidth;
                        float sizeW, sizeH;
            if (ivHeight != ivWidth) {
                sizeH = bmHeight * scale;
                sizeW = bmWidth * scale;
            } else {
                sizeH = bmHeight > bmWidth ? bmWidth : bmHeight;
                sizeW = bmHeight > bmWidth ? bmWidth : bmHeight;
            }

            int left = (int) ((bmWidth - sizeW) / 2);
            int top = (int) ((bmHeight- sizeH) / 2);
            if (bmHeight > bmWidth) {
                left = 0;
            } else if (bmHeight < bmWidth) {
                top = 0;
            }
// TODO: 7/3/2018 optimize for out of memory
            bitmap = Bitmap.createBitmap(bitmap, left, top, (int) sizeW, (int) sizeH);
            bitmap = Bitmap.createScaledBitmap(bitmap, ivWidth, ivHeight, false);
        } else {
//            if (bitmap.getWidth() != width || bitmap.getHeight() != height)
//                finalBitmap = Bitmap.createScaledBitmap(bitmap, width, width, false);
//            else
//            finalBitmap = bitmap;
        }
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight());
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));

        canvas.drawRoundRect(rectF, cornerX, cornerY, paint);

//        canvas.drawCircle(finalBitmap.getWidth() / 2 + 0.7f, finalBitmap.getHeight() / 2 + 0.7f, finalBitmap.getWidth() / 2 + 0.1f,
//                paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rectF, paint);
        return output;
    }

    public static Bitmap getOvalCroppedBitmap(Bitmap bitmap, int radius, float index) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);
        paint.setDither(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        RectF oval = new RectF(0, 0, (int) (finalBitmap.getWidth() / index), finalBitmap.getHeight());
        canvas.drawOval(oval, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, oval, paint);

        return output;
    }

    public static Bitmap getTriangleCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        Point point1_draw = new Point(finalBitmap.getWidth() / 2, 0);
        Point point2_draw = new Point(0, finalBitmap.getHeight());
        Point point3_draw = new Point(finalBitmap.getWidth(), finalBitmap.getHeight());

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point1_draw.x, point1_draw.y);
        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getHexagonCroppedBitmap(Bitmap bitmap, int radius) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int h = finalBitmap.getHeight();
        int w = finalBitmap.getWidth();
        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        Point point1_draw = new Point(w / 2, 0);
        Point point2_draw = new Point(0, (int) (h * 0.33));// 1/3
        Point point3_draw = new Point(0, (int) (h * 0.66));// 2/3
        Point point4_draw = new Point(w / 2, h);
        Point point5_draw = new Point(w, (int) (h * 0.66));// 2/3
        Point point6_draw = new Point(w, (int) (h * 0.33));// 1/3

        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point4_draw.x, point4_draw.y);
        path.lineTo(point5_draw.x, point5_draw.y);
        path.lineTo(point6_draw.x, point6_draw.y);

        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap getOctagonCroppedBitmap(Bitmap bitmap, int radius, float corner) {
        Bitmap finalBitmap;
        if (bitmap.getWidth() != radius || bitmap.getHeight() != radius)
            finalBitmap = Bitmap.createScaledBitmap(bitmap, radius, radius, false);
        else
            finalBitmap = bitmap;
        Bitmap output = Bitmap.createBitmap(finalBitmap.getWidth(), finalBitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int h = finalBitmap.getHeight();
        int w = finalBitmap.getWidth();
        Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, finalBitmap.getWidth(), finalBitmap.getHeight());

        float biggerCorner = 1 - corner;
        Point point1_draw = new Point((int) (w * biggerCorner), 0);
        Point point2_draw = new Point((int) (w * corner), 0);
        Point point3_draw = new Point(0, (int) (h * corner));// 1/3
        Point point4_draw = new Point(0, (int) (h * biggerCorner));// 2/3
        Point point5_draw = new Point((int) (w * corner), h);
        Point point6_draw = new Point((int) (w * biggerCorner), h);
        Point point7_draw = new Point(w, (int) (h * biggerCorner));// 2/3
        Point point8_draw = new Point(w, (int) (h * corner));// 1/3


        Path path = new Path();
        path.moveTo(point1_draw.x, point1_draw.y);
        path.lineTo(point2_draw.x, point2_draw.y);
        path.lineTo(point3_draw.x, point3_draw.y);
        path.lineTo(point4_draw.x, point4_draw.y);
        path.lineTo(point5_draw.x, point5_draw.y);
        path.lineTo(point6_draw.x, point6_draw.y);
        path.lineTo(point7_draw.x, point7_draw.y);
        path.lineTo(point8_draw.x, point8_draw.y);

        path.close();
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.parseColor("#BAB399"));
        canvas.drawPath(path, paint);
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(finalBitmap, rect, rect, paint);

        return output;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public float getOvalWideIndex() {
        return ovalWideIndex;
    }

    public void setOvalWideIndex(float ovalWideIndex) {
        this.ovalWideIndex = ovalWideIndex;
    }

    public float getOctaCornerIndex() {
        return octaCornerIndex;
    }

    public void setOctaCornerIndex(float octaCornerIndex) {
        this.octaCornerIndex = octaCornerIndex;
    }

    public float getCornerX() {
        return cornerX;
    }

    public void setCornerX(float cornerX) {
        this.cornerX = cornerX;
    }

    public float getCornerY() {
        return cornerY;
    }

    public void setCornerY(float cornerY) {
        this.cornerY = cornerY;
    }
}
