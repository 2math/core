package com.futurist_labs.android.base_library.model;

import android.content.DialogInterface;

/**
 * Created by Galeen on 12/21/2017.
 */

public class ProgressData {
    private String loadingMsg;
    private DialogInterface.OnCancelListener onCancelListener;

    public ProgressData(String loadingMsg, DialogInterface.OnCancelListener onCancelListener) {
        this.loadingMsg = loadingMsg;
        this.onCancelListener = onCancelListener;
    }

    public String getLoadingMsg() {
        return loadingMsg;
    }

    public DialogInterface.OnCancelListener getOnCancelListener() {
        return onCancelListener;
    }
}
