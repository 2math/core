package com.futurist_labs.android.base_library.views.progress;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.futurist_labs.android.base_library.R;


/**
 * Created by Galeen on 1/9/2018.
 */

public class AppProgressView extends FrameLayout {
    public AppProgressView(@NonNull Context context) {
        super(context);
        init();
    }

    public AppProgressView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AppProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AppProgressView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.progress, this);
        viewBottom = findViewById(R.id.viewBottom);
        viewTop = findViewById(R.id.viewTop);

//        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                Log.e("progress","onGlobalLayout");
//                handleVisibility(getVisibility());
//            }
//        });
    }

    private void handleVisibility(int visibility) {
        if (visibility == VISIBLE) {
            startAnimation();
        } else {
            stopAnimation();
        }
    }

    @Override
    protected void onVisibilityChanged(@NonNull View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
//        Log.e("progress","onVisibilityChanged");
        handleVisibility(visibility);
    }

    ImageView viewBottom, viewTop;

    AnimatorSet animationSet;
    int animationDuration = 1000;

    private void startAnimation(final View mViewTop, final View mViewBottom) {
        // This ObjectAnimator uses the path to change the x and y scale of the mView object.
        ObjectAnimator animator1X = ObjectAnimator.ofFloat(mViewTop, "scaleX", 1, 0);
        // Set the duration and interpolator for this animation
        animator1X.setDuration(animationDuration);
//        animator1X.setInterpolator(interpolator);
        ObjectAnimator animator1Y = ObjectAnimator.ofFloat(mViewTop, "scaleY", 1, 0);
        animator1Y.setDuration(animationDuration);

        ObjectAnimator animator2X = ObjectAnimator.ofFloat(mViewBottom, "scaleX", 0, 1);
        animator2X.setDuration(animationDuration);
        ObjectAnimator animator2Y = ObjectAnimator.ofFloat(mViewBottom, "scaleY", 0, 1);
        animator2Y.setDuration(animationDuration);

        stopAnimation();
        animationSet = new AnimatorSet();
        animationSet.playTogether(animator1X, animator1Y, animator2X, animator2Y);

        animationSet.addListener(new Animator.AnimatorListener() {
            boolean isCanceled = false;

            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (!isCanceled) {
                    mViewBottom.bringToFront();
                    //swap views
                    startAnimation(mViewBottom, mViewTop);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                isCanceled = true;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        animationSet.start();
//        Log.e("progress","startAnimation");
    }
    public void startAnimation() {
        startAnimation(viewBottom, viewTop);
    }
    public void stopAnimation() {
        if (animationSet != null && animationSet.isStarted()) {
            animationSet.cancel();
//            Log.e("progress","stopAnimation");
        }
    }
}
