package com.criapp_studio.coreapp.repository;

import com.criapp_studio.coreapp.model.unguarded.User;
import com.criapp_studio.coreapp.repository.persistence.AppPersistenceManager;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;

/**
 * Created by Galeen on 9/5/2018.
 * mock this calls for Unit tests
 */
public class MockRepository implements Repository {
    @Override
    public void saveCredentials(String username, String pass) {

    }

    @Override
    public String[] getCredentials() {
        return new String[0];
    }

    @Override
    public String getToken() {
        return null;
    }

    @Override
    public void setToken(String token) {

    }

    @Override
    public void saveUser(User user) {

    }

    @Override
    public void saveUser(NetworkResponse response) {

    }

    @Override
    public void getUser(AppPersistenceManager.DataEvents<User> callback) {

    }

    @Override
    public void login(MainCallback mCallback, String mail, String pass) {

    }
}
