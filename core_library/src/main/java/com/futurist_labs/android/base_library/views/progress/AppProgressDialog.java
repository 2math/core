package com.futurist_labs.android.base_library.views.progress;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.futurist_labs.android.base_library.R;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleRes;


/**
 * Created by Galeen on 1/9/2018.
 */

public class AppProgressDialog extends Dialog {
    private AppProgressView progress;
    private TextView tvProgressMsg;

    public AppProgressDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public AppProgressDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected AppProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private void init(Context context) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(false);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.progess_dialog, null);
        setContentView(view);
        progress = view.findViewById(R.id.progressView);
        tvProgressMsg = view.findViewById(R.id.tvProgressMsg);
//        setOnDismissListener(new OnDismissListener() {
//            @Override
//            public void onDismiss(DialogInterface dialogInterface) {
//                progress.stopAnimation();
//            }
//        });
    }

//    public void showDialog() {
//        progress.startAnimation();
//    }
//
//    public void dismissAppDialog() {
//        progress.stopAnimation();
//        dismiss();
//    }

    public void setMessage(String msg){
        tvProgressMsg.setText(msg);
    }

    @Override
    public void show() {
        super.show();
        progress.startAnimation();
    }

    @Override
    public void dismiss() {
        progress.stopAnimation();
        super.dismiss();
    }
}