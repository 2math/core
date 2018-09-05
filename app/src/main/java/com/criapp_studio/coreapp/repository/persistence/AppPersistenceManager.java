package com.criapp_studio.coreapp.repository.persistence;

import com.criapp_studio.coreapp.model.unguarded.Login;
import com.criapp_studio.coreapp.model.unguarded.User;
import com.criapp_studio.coreapp.repository.json.AppJsonParser;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.repository.persistence.BasePersistenceManager;
import com.futurist_labs.android.base_library.repository.persistence.GetDataCallback;
import com.futurist_labs.android.base_library.repository.persistence.SetDataCallback;


/**
 * Created by Galeen on 28.9.2016 г..
 */
public class AppPersistenceManager {


//    public static String getLastModified() {
//        return SharedPreferencesManager.getLastModified();
//    }

    public static void saveCredentials(String username, String pass) {
        BasePersistenceManager.addStringValue(DbConstants.USERNAME, username);
        BasePersistenceManager.addStringValue(DbConstants.PASS, pass);
    }

    /**
     * @return String[0] = username, String[1] = password
     */

    public static String[] getCredentials() {
        String[] res = new String[2];
        res[0] = BasePersistenceManager.getString(DbConstants.USERNAME, null, true);
        res[1] = BasePersistenceManager.getString(DbConstants.PASS, null, true);
        return res;
    }

    public static String getToken() {
        return BasePersistenceManager.getToken();
    }

    public static void setToken(String token) {
        BasePersistenceManager.saveToken(token);
    }



    public static void saveUser(User user) {
        BasePersistenceManager.addValue(DbConstants.USER, user,
                new SetDataCallback<User>() {
                    @Override
                    public String parseData(User user) {
                            return AppJsonParser.writeUser(user);
                    }
                }, true);
    }

    public static void saveUser(NetworkResponse response) {
        if (response != null) {
                Login login = AppJsonParser.readLogin(response.json);
                if (login != null) {
                    BasePersistenceManager.saveToken(login.getSessionId());
                    response.object = login.getUser();
                }
            saveUser((User) response.object);
        }
    }

    public static void getUser(final DataEvents<User> callback) {
        if (callback == null) return;
        BasePersistenceManager.get(DbConstants.USER,
                new GetDataCallback<User>() {
                    @Override
                    public void onData(User data) {
                        callback.onData(data);
                    }

                    @Override
                    public User parseData(String dataToParse) {
                            return AppJsonParser.readUser(dataToParse);
                    }
                }, true);
    }

    public interface DataEvents<T> {
        void onData(T t);
    }
}
