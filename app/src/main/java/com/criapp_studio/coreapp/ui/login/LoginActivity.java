package com.criapp_studio.coreapp.ui.login;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.criapp_studio.coreapp.BuildConfig;
import com.criapp_studio.coreapp.R;
import com.futurist_labs.android.base_library.repository.network.Action;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.ui.BaseActivity;
import com.futurist_labs.android.base_library.ui.versions.UpdateDialogFragment;
import com.futurist_labs.android.base_library.utils.FragmentUtils;
import com.futurist_labs.android.base_library.utils.versions.VersionsUtil;
import com.futurist_labs.android.base_library.views.font_views.FontTextView;

public class LoginActivity extends BaseActivity<LoginViewModel> implements LoginFragment.OnFragmentInteractionListener, UpdateDialogFragment.Callback {

    private FrameLayout flContainer;
    private FontTextView tvHello;
    private boolean initOnResume = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();

        if (savedInstanceState == null) {//else is recreate
            new VersionsUtil().checkVersions(this, activityViewModel.getNetworkCallback(),
                    new Action(Action.GET_UNAUTHORIZED, "http://www.mocky.io/v2/5d6525d23400002c00f44600", null)
                            .setIsCheckServerUrl(false)
                            .setFullUrl(true),
                    MainCallback.TYPE_DIALOG, BuildConfig.VERSION_CODE,
                    new VersionsUtil.Callback() {
                        @Override
                        public void onEnd(int status) {
                            if (status != VersionsUtil.ACTION_MUST_UPDATE && status != VersionsUtil.ACTION_CAN_UPDATE) {
                                activityViewModel.init();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (initOnResume) {
            initOnResume = false;
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

    @Override
    public void onDismiss() {
        activityViewModel.init();
    }

    @Override
    public void goToStore() {
        initOnResume = true;
    }
}
