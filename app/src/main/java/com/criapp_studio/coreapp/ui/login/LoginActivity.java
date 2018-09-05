package com.criapp_studio.coreapp.ui.login;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.criapp_studio.coreapp.R;
import com.futurist_labs.android.base_library.ui.BaseActivity;
import com.futurist_labs.android.base_library.utils.FragmentUtils;
import com.futurist_labs.android.base_library.views.font_views.FontTextView;

public class LoginActivity extends BaseActivity<LoginViewModel> implements LoginFragment.OnFragmentInteractionListener {

    private FrameLayout flContainer;
    private FontTextView tvHello;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        if (savedInstanceState == null) {//else is recreate
            activityViewModel.init();
        }
    }

    @Override
    protected void setObservers() {
        activityViewModel.getOnShowLogin().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isOK) {
                if (isOK != null && isOK) {
                    showLogin();
                }
            }
        });

        activityViewModel.getOnShowMain().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean isOK) {
                if (isOK != null && isOK) {
                    goToResult();
                }
            }
        });
    }

    @NonNull
    @Override
    protected Class<LoginViewModel> getViewModelClass() {
        return LoginViewModel.class;
    }

    @Override
    public void doLogin(String username, String password) {
        activityViewModel.doLogin(username, password);
    }

    private void showLogin() {
        tvHello.setVisibility(View.GONE);
        if (!(topFragment instanceof LoginFragment)) {
            getFragmentBuilder().addToBackStack(false).replace(new LoginFragment());
        } else {
            getFragmentBuilder().clearBackStack();
        }
    }

    private FragmentUtils.FragmentBuilder getFragmentBuilder() {
        return FragmentUtils.createBuilder(this).containerId(R.id.flContainer);
    }

    private void goToResult() {

    }

    private void initView() {
        flContainer = findViewById(R.id.flContainer);
        tvHello = findViewById(R.id.tvHello);
    }
}
