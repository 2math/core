package com.futurist_labs.android.base_library.utils;

import android.graphics.Color;
import android.view.View;

import com.balysv.materialripple.MaterialRippleLayout;

/**
 * Created by Galeen on 3.5.2016 г..
 */
public class RippleUtils {
    private static final int CORNERS = 6;//in dp
    static final int RIPPLE_DURATION = 250;

    public static View setRippleEffectSquareInAdapter(View view) {
        return MaterialRippleLayout.on(view).rippleColor(Color.BLACK)
                .rippleAlpha(0.2f)
                .rippleDuration(RIPPLE_DURATION)
                .rippleOverlay(true)
                .rippleDelayClick(true)
                .rippleHover(true).create();
    }

    public static View setRippleEffectSquare(View view) {
            return setRippleEffect(view, Color.BLACK, RIPPLE_DURATION, 0, 0.2f);
    }

    public static void setRippleEffectSquare(View... views) {
        for (View view : views) {
            setRippleEffectSquare(view);
        }
    }

    /**
     * @param view          will set ripple around
     * @param color         ripple color
     * @param durationMills animation time
     * @param radiusDP      corners will be counted as DP
     * @param alpha         alpha from 0.0 - 1.0
     */
    public static View setRippleEffect(View view, int color, int durationMills, int radiusDP, float alpha) {
            return  MaterialRippleLayout.on(view)
                    .rippleColor(color)
                    .rippleAlpha(alpha)
                    .rippleDuration(durationMills)
                    .rippleOverlay(true)
                    .rippleDelayClick(true)
                    .rippleHover(true)
                    .rippleRoundedCorners(radiusDP)
                    .create();
    }

    public static View setRippleEffect(View view) {
           return setRippleEffect(view, Color.BLACK, RIPPLE_DURATION, CORNERS, 0.2f);
    }

    public static void setRippleEffectCircle(View view) {
            setRippleEffect(view, Color.BLACK, RIPPLE_DURATION, 100, 0.2f);
    }

    public static View setRippleEffectCircleInAdapter(View view) {
        if (view != null)
            return MaterialRippleLayout.on(view).rippleColor(Color.BLACK).rippleAlpha(0.2f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
                    .rippleHover(true).rippleRoundedCorners(100).create();
        return null;
    }

    public static View setRippleEffectCircleWhite(View view) {
        if (view != null)
            return MaterialRippleLayout.on(view).rippleColor(Color.WHITE).rippleAlpha(0.9f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
                    .rippleHover(true).rippleRoundedCorners(100).create();
        return null;
    }

    public static void setRippleEffect(View view, int color) {
        if (view != null)
            MaterialRippleLayout.on(view).rippleColor(color).rippleAlpha(0.2f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
                    .rippleHover(true).create();
    }

    public static void setRippleEffect(View... views) {
        for (View view : views) {
            setRippleEffect(view);
        }
    }

    public static void setRippleEffectWhite(View view) {
        if (view != null)
            MaterialRippleLayout.on(view).rippleColor(Color.WHITE).rippleAlpha(0.9f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
                    .rippleHover(true).rippleRoundedCorners(CORNERS).create();
    }

    public static void setRippleEffectSquareWhite(View view) {
        if (view != null)
            setRippleEffect(view, Color.WHITE, RIPPLE_DURATION, 0, 0.2f);
//            MaterialRippleLayout.on(view).rippleColor(Color.WHITE).rippleAlpha(0.9f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
//                    .rippleHover(true).create();
    }

    public static void setRippleEffectSquareWhite(View... views) {
        for (View view : views) {
            setRippleEffectSquareWhite(view);
        }
    }

    /**
     * @param view
     * @param color
     * @param radiusDp if -1 default CORNERS is used
     */
    public static void setRippleEffectCustomColor(View view, int color, int radiusDp) {
        if (view != null) {
            if (radiusDp == -1) radiusDp = CORNERS;
            MaterialRippleLayout.on(view).rippleColor(color).rippleAlpha(0.9f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
                    .rippleHover(true).rippleRoundedCorners(CORNERS).create();
        }
    }

    public static void setRippleEffectSquareCustomColor(View view, int color) {
        if (view != null)
            setRippleEffect(view, color, RIPPLE_DURATION, 0, 0.9f);
//            MaterialRippleLayout.on(view).rippleColor(color).rippleAlpha(0.9f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
//                    .rippleHover(true).create();
    }

    public static void setRippleEffectSquareCustomColor(int color, View... views) {
        for (View view : views) {
            setRippleEffectSquareCustomColor(view, color);
        }
    }

    public static void setRippleEffectLessRounded(View view) {
        if (view != null) {
            setRippleEffect(view, Color.BLACK, RIPPLE_DURATION, 4, 0.2f);
        }
//        MaterialRippleLayout.on(view).rippleColor(Color.BLACK).rippleAlpha(0.2f).rippleDuration(RIPPLE_DURATION).rippleOverlay(true).rippleDelayClick(true)
//                .rippleHover(true).rippleRoundedCorners(4).create();
    }

}
