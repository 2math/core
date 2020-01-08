package com.futurist_labs.android.base_library.views.font_views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

import androidx.collection.ArrayMap;


/**
 * Created by Galeen on 9.5.2016 г..
 * Custom TextView to easy set fonts
 */
public class FontHelper {
    private static final String ANDROID_SCHEMA = "http://schemas.android.com/apk/res/android";
    private static final String FONT_ASSETS = "file:///android_asset/";
    private static final int NO_STYLE = -1;

    private static String boldFont = "fonts/Comfortaa-Bold.ttf";
    private String regularFont = BaseLibraryConfiguration.getInstance().getRegularFont();
    private static String italicFont = "fonts/FuturaLT.ttf";
    private static String lightFont = "fonts/Comfortaa-Light.ttf";
    private static String awesome = "fonts/fa-solid-900.ttf";
    public static final int BOLD = 1;
    public static final int NORMAL = 0;
    public static final int ITALIC = 2;
    public static final int LIGHT = 3;
    public static final int AWESOME = 4;
    private static ArrayMap<String, Typeface> fontCache = new ArrayMap<>();
    private FontType type = FontType.REGULAR;
    private TextView view;
    private boolean addStrike = false;
    private Paint paint;
    private int strikeColor = Color.GRAY;

    public static Typeface getTypeface(String fontName, Context context) {
        Typeface typeface = fontCache.get(fontName);

        if (typeface == null) {
            try {
                typeface = Typeface.createFromAsset(context.getAssets(), fontName);
            } catch (Exception e) {
                return null;
            }

            fontCache.put(fontName, typeface);
        }

        return typeface;
    }

    public static String setCustomFontForWebView(String htmlString, String path, float textSize, String color, String
            linkColorHex) {
        if (linkColorHex != null) {
            linkColorHex = String.format("a{color: %s}", linkColorHex);
        } else {
            linkColorHex = "";
        }
        return "<html>" +
                "<head>" +
                "<style type=\"text/css\">" +
                "@font-face {" +
                "font-family: MyFont;" +
                "src: url(\"" + path + "\")" +
                "}" +
                "body {" +
                "font-family: MyFont;" +
                "font-size: " + textSize + "px;" +
//                "text-align: justify;" +
                "color: " + color + ";" +
                "}" +
                "img{display: inline;height: auto;max-width: 100%;}" +
                linkColorHex +
                "</style>" +
                "</head>" +
                "<body>" + htmlString +
                "</body>" +
                "</html>";
    }

    public static String setCustomFontForWebView(String htmlString, int type, float textSize, String color, String
            linkColorHex) {
        if (color == null) {
            color = "#989898";
        }
        String font = FONT_ASSETS;
        switch (type) {
            case BOLD:
                font += boldFont;
//                view.setTypeface(getTypeface(boldFont, view.getContext()), Typeface.BOLD);
                break;
            case ITALIC:
                font += italicFont;
                break;
            case LIGHT:
                font += lightFont;
                break;
            case AWESOME:
                font += awesome;
                break;
            default:
                font += "fonts/System_San_Francisco_Display_Regular.ttf";
                break;
        }
        return setCustomFontForWebView(htmlString, font, textSize, color, linkColorHex);
    }

    public static String getItalicFont() {
        return italicFont;
    }

    public static String getLightFont() {
        return lightFont;
    }

    public static String getAwesome() {
        return awesome;
    }

    public static void init(String bold, String italic, String light) {
        boldFont = bold;
        italicFont = italic;
        lightFont = light;
    }

    public static void setAwesome(String font) {
        awesome = font;
    }

    private StyleAttributes attr;

    FontHelper(TextView view, StyleAttributes attr) {
        this.view = view;
        this.attr = attr;
    }

    private int androidTextStyle = NO_STYLE;

    void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.getTheme().obtainStyledAttributes(
                    attrs, attr.getAttr(), 0, 0);
            try {
                if (a.getString(attr.getTvFont()) != null) {
                    String fontName = a.getString(attr.getTvFont());
                    regularFont = "fonts/" + fontName;
                    type = FontType.REGULAR;
                    //option to set this custom font to bold or italic
                    androidTextStyle = attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL);
                } else {
                    if (a.getInt(attr.getTvType(), -1) != -1) {
                        type = FontType.fromType(a.getInt(attr.getTvType(), 0));
                    } else { // if nothing extra was used, fall back to regular android:textStyle parameter
                        type = FontType.fromType(attrs.getAttributeIntValue(ANDROID_SCHEMA, "textStyle", Typeface.NORMAL));
                    }
                }
                setViewFont(type);
                addStrike = a.getBoolean(attr.getStrike(), false);
                strikeColor = a.getColor(attr.getStrikeColor(), Color.GRAY);
                view.setPaintFlags(view.getPaintFlags() | Paint.SUBPIXEL_TEXT_FLAG);
                paint = new Paint();
                paint.setColor(strikeColor);
                paint.setStrokeWidth(view.getResources().getDisplayMetrics().density * 1);
            } finally {
                a.recycle();
            }
        }
//        if (view.getTextSize() == view.getContext().getResources().getDimensionPixelSize(R.dimen.sp14)) {
//            view.setTextSize(view.getContext().getResources().getDimensionPixelSize(R.dimen.text_normal));
//        }
    }

    void onDraw(Canvas canvas) {
        if (addStrike && view.getText().length() > 0) {// TODO: 12/9/2018 calculate padding, measure text size!
            canvas.drawLine(0, view.getHeight() / 2, view.getWidth(),
                    view.getHeight() / 2, paint);
        }
    }

    public void setViewFont(FontType type) {
        if (view == null) {
            return;
        }

        switch (type) {
            case BOLD:
                applyFont(boldFont, Typeface.BOLD);
                break;
            case ITALIC:
                applyFont(italicFont, Typeface.ITALIC);
                break;
            case BOLD_ITALIC:
                applyBoldItalic();
                break;
            case LIGHT:
                applyFont(lightFont == null ? italicFont : lightFont, Typeface.NORMAL);
                break;
            case AWESOME:
                applyFont(awesome, Typeface.NORMAL);
                break;
            case OS_BOLD:
                applyFont(boldFont, Typeface.BOLD);
                break;
            case OS_ITALIC:
                applyFont(italicFont, Typeface.ITALIC);
                break;
            case OS_BOLD_ITALIC:
                applyBoldItalic();
                break;
            default:
                applyFont(regularFont, androidTextStyle != NO_STYLE ? androidTextStyle : Typeface.NORMAL);
                break;
        }
    }

    public void setViewFont(boolean goBold) {
        if (view == null) {
            return;
        }
//        if (goBold)
//            setTypeface(null, Typeface.BOLD);
//        else
//            setTypeface(null, Typeface.NORMAL);

        if (goBold) {
            setViewFont(FontType.BOLD);
        } else {
            setViewFont(FontType.REGULAR);
        }
    }

    public static String getBoldFont() {
        return boldFont;
    }

    public String getRegularFont() {
        return regularFont;
    }

    public void setRegularFont(String regularFont) {
        this.regularFont = regularFont;
    }

    private void applyFont(String fontName, int style) {
        Typeface font = getTypeface(fontName, view.getContext());
        if (font == null) {//if we don't have such a font we get the regular one and apply style on it
            font = getTypeface(regularFont, view.getContext());
            view.setTypeface(font, style);
        } else {
            view.setTypeface(font);
        }

    }

    private void applyBoldItalic() {
        Typeface font = getTypeface(boldFont, view.getContext());
        if (font == null) {//no bold font
            font = getTypeface(italicFont, view.getContext());
            if (font == null) {//if we don't have italic too, we get the regular one and apply BOLD_ITALIC on it
                font = getTypeface(regularFont, view.getContext());
                view.setTypeface(font, Typeface.BOLD_ITALIC);
            } else {//make italic bold
                view.setTypeface(font, Typeface.BOLD);
            }
        } else {//make bold font italic
            view.setTypeface(font, Typeface.ITALIC);
        }
    }

    public enum FontType {
        OS_NORMAL(0),
        OS_BOLD(1),//        Typeface.BOLD
        OS_ITALIC(2),
        OS_BOLD_ITALIC(3),
        REGULAR(10),
        BOLD(11),
        ITALIC(12),
        BOLD_ITALIC(15),
        LIGHT(13),
        AWESOME(14);

        int type;

        FontType(int type) {
            this.type = type;
        }

        public int getValue() {
            return type;
        }

        static FontHelper.FontType fromType(int type) {
            for (FontHelper.FontType drf : values()) {
                if (drf.type == type) {
                    return drf;
                }
            }
            throw new IllegalArgumentException();
        }
    }

    public static class StyleAttributes {
        private int[] attr;
        private int tvFont, tvType, strike, strikeColor;

        public StyleAttributes(int[] attr, int tvFont, int tvType) {
            this.attr = attr;
            this.tvFont = tvFont;
            this.tvType = tvType;
        }

        public StyleAttributes(int[] attr, int tvFont, int tvType, int strike, int strikeColor) {
            this.attr = attr;
            this.tvFont = tvFont;
            this.tvType = tvType;
            this.strike = strike;
            this.strikeColor = strikeColor;
        }

        public int[] getAttr() {
            return attr;
        }

        public int getTvFont() {
            return tvFont;
        }

        public int getTvType() {
            return tvType;
        }

        public int getStrike() {
            return strike;
        }

        public int getStrikeColor() {
            return strikeColor;
        }
    }
}
