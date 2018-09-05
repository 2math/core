package com.criapp_studio.coreapp.repository;

import com.criapp_studio.coreapp.model.unguarded.User;
import com.criapp_studio.coreapp.repository.persistence.AppPersistenceManager;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;

/**
 * Created by Galeen on 9/5/2018.
 */
public interface Repository {
    //local
    void saveCredentials(String username, String pass);
    String[] getCredentials();
    String getToken();
    void setToken(String token);
    void saveUser(User user);
    void saveUser(NetworkResponse response);
    void getUser(AppPersistenceManager.DataEvents<User> callback);

    //remote
    void login(MainCallback mCallback, String mail, String pass);
}
