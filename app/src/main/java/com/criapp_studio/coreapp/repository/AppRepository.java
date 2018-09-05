package com.criapp_studio.coreapp.repository;

import com.criapp_studio.coreapp.model.unguarded.User;
import com.criapp_studio.coreapp.repository.network.AppNetworkManager;
import com.criapp_studio.coreapp.repository.persistence.AppPersistenceManager;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;

/**
 * Created by Galeen on 9/5/2018.
 */
public class AppRepository implements Repository {
    @Override
    public void saveCredentials(String username, String pass) {
        AppPersistenceManager.saveCredentials(username, pass);
    }

    @Override
    public String[] getCredentials() {
        return AppPersistenceManager.getCredentials();
    }

    @Override
    public String getToken() {
        return AppPersistenceManager.getToken();
    }

    @Override
    public void setToken(String token) {
        AppPersistenceManager.setToken(token);
    }

    @Override
    public void saveUser(User user) {
        AppPersistenceManager.saveUser(user);
    }

    @Override
    public void saveUser(NetworkResponse response) {
        AppPersistenceManager.saveUser(response);
    }

    @Override
    public void getUser(AppPersistenceManager.DataEvents<User> callback) {
        AppPersistenceManager.getUser(callback);
    }

    @Override
    public void login(MainCallback mCallback, String mail, String pass) {
        AppNetworkManager.login(mCallback, mail, pass);
    }
}
