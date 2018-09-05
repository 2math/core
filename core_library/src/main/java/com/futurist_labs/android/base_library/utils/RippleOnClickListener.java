package com.futurist_labs.android.base_library.utils;

import android.view.View;

/**
 * Created by Galeen on 31.5.2017 г..
 * Wrapper over View.OnClickListener to prevent clicking while ripple is animating
 */

public abstract class RippleOnClickListener implements View.OnClickListener {
    private long clickedOn = 0;
    private long blockDuration = RippleUtils.RIPPLE_DURATION*2;

    public RippleOnClickListener() {    }

    /**
     * Set time while doble clicking will be disabled
     * @param blockDuration - mills
     */
    public RippleOnClickListener(long blockDuration) {
        this.blockDuration = blockDuration;
    }

    @Override
    public void onClick(View view) {
//        LogUtils.d("RippleOnClickListener","now "+System.currentTimeMillis() +" clickedOn "+clickedOn + " System" +
//                ".currentTimeMillis() - clickedOn "+(System.currentTimeMillis() - clickedOn)+" RippleUtils" +
//                ".RIPPLE_DURATION*2 "+blockDuration);
        if (System.currentTimeMillis() - clickedOn > blockDuration) {
            onClickListener(view);
        }
        clickedOn = System.currentTimeMillis();
    }

    /**
     * Same as View.OnClickListener.OnClick
     * @param clickedView
     */
    abstract public void onClickListener(View clickedView);
}
