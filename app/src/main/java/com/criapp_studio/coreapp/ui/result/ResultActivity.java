package com.criapp_studio.coreapp.ui.result;

import android.os.Bundle;
import android.view.View;

import com.criapp_studio.coreapp.R;
import com.criapp_studio.coreapp.ui.login.LoginViewModel;
import com.futurist_labs.android.base_library.ui.BaseActivity;
import com.futurist_labs.android.base_library.utils.RippleUtils;
import com.futurist_labs.android.base_library.views.font_views.FontTextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;

public class ResultActivity extends BaseActivity<LoginViewModel> {

    private FontTextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        initView();

        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activityViewModel.logout();
            }
        });
    }

    @Override
    protected void setObservers() {
        activityViewModel.getOnLogoutLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isOK) {
                if(isOK!=null && isOK){
                    finish();
                }
            }
        });
    }

    @NonNull
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    private void initView() {
        tvLogout = findViewById(R.id.tvLogout);
        RippleUtils.setRippleEffectWhite(tvLogout);
    }
}
