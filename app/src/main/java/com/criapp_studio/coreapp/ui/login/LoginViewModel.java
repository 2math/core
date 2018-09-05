package com.criapp_studio.coreapp.ui.login;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.criapp_studio.coreapp.R;
import com.criapp_studio.coreapp.model.unguarded.User;
import com.criapp_studio.coreapp.repository.Repository;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.ui.BaseEvents;
import com.futurist_labs.android.base_library.ui.BaseViewModelWithRepository;

/**
 * Created by Galeen on 5/23/18.
 */
public class LoginViewModel extends BaseViewModelWithRepository<Repository> {
    private MutableLiveData<Boolean> onShowLogin;
    private MutableLiveData<Boolean> onShowMain;
    private MutableLiveData<Boolean> onChangePassword;

    public LiveData<Boolean> getOnShowLogin() {
        if (onShowLogin == null) onShowLogin = new MutableLiveData<>();
        return onShowLogin;
    }

    public LiveData<Boolean> getOnShowMain() {
        if (onShowMain == null) onShowMain = new MutableLiveData<>();
        return onShowMain;
    }

    @Override
    protected void init() {

        String[] creds = repository.getCredentials();
        if (creds[0] != null) {
            doLogin(creds[0], creds[1]);
        } else {
            if (onShowLogin != null) onShowLogin.setValue(true);
        }
    }

    public void doLogin(String username, String password) {
        repository.login(new LoginCallback(getNetworkCallback(), MainCallback.TYPE_DIALOG, username, password), username, password);
    }

    class LoginCallback extends MainCallback {
        String username;
        String password;

        public LoginCallback(BaseEvents presenterCallback, int progressType, String username, String password) {
            super(presenterCallback, progressType);
            this.username = username;
            this.password = password;
        }

        @Override
        public void inTheEndOfDoInBackground(NetworkResponse response) {
            super.inTheEndOfDoInBackground(response);
            repository.saveUser(response);
        }

        @Override
        public void onPostExecute(NetworkResponse response) {
            super.onPostExecute(response);

            User user = (User) response.object;
            if (user != null) {
                repository.saveCredentials(username, password);
//                NetworkManager.postToken(new MainCallback(LoginActivity.this, false), PersistenceManager.getDeviceID(),
//                        FirebaseInstanceId.getInstance().getToken());
                if (onShowMain != null) {
                    onShowMain.setValue(false);
                }
            } else {
                response.errorMsg = R.string.err_wrong_login;
                getNetworkCallback().showError("", response);
                if (onShowLogin != null) {
                    onShowLogin.setValue(false);
                }
            }
        }

        @Override
        public void onError(String msg, NetworkResponse response) {

            if (response != null) {
                response.errorMsg = R.string.err_wrong_login;
            }
            super.onError(msg, response);
//            DialogUtils.showError(activity.get(), R.string.err_wrong_login);
            if (onShowLogin != null) {
                onShowLogin.setValue(true);
            }
        }
    }


}
