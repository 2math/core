package com.futurist_labs.android.base_library.repository.persistence;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


/**
 * Created by Galeen on 8.4.2016 г..
 * holds the data coming from the server as JSON.
 */
public class BaseSharedPreferenceManager implements BasePersistenceInterface {
    private static final String PREF_TOKEN = "PREF_TOKEN";
    private static final String PREF_MAIL = "PREF_MAIL";
    private static final String KEYS_USER = "@keys$@";
    private static final String KEYS_MAIN = "@main$@";


    private static SharedPreferences prefs;
    // TODO: 5/21/18 add secure prefs

    /**
     * @return Application's {@code SharedPreferences}.
     */
    private static SharedPreferences getPrefs() {
        if (prefs == null) {
            prefs = BaseLibraryConfiguration.getInstance().getApplication().getSharedPreferences("app", Context.MODE_PRIVATE);
        }
        return prefs;
    }

    private static SharedPreferences.Editor getEditor() {
        return getPrefs().edit();
    }

    @Override
    public boolean useCache() {
        return true;
    }

    /**
     * It will parse the data to string and save it both in background
     *
     * @param key
     * @param value
     * @param callback
     */
    @Override
    public <T> void save(final String key, final T value, final SetDataCallback callback, final boolean isUser) {
        if (callback != null) {
            new Thread(
                    new Runnable() {
                        @Override
                        public void run() {
                            save(key, callback.parseData(value), isUser);
                        }
                    }
            ).run();
        }
    }


    @Override
    public void save(String key, String value, boolean isUser) {
        getEditor().putString(key, value).apply();
        addToUserKeys(key, isUser);
    }

    @Override
    public void save(String key, boolean value, boolean isUser) {
        getEditor().putBoolean(key, value).apply();
        addToUserKeys(key, isUser);
    }

    @Override
    public void save(String key, int value, boolean isUser) {
        getEditor().putInt(key, value).apply();
        addToUserKeys(key, isUser);
    }

    @Override
    public void save(String key, float value, boolean isUser) {
        getEditor().putFloat(key, value).apply();
        addToUserKeys(key, isUser);
    }

    @Override
    public void save(String key, long value, boolean isUser) {
        getEditor().putLong(key, value).apply();
        addToUserKeys(key, isUser);
    }

    @Override
    public <T> void get(String key, GetDataCallback<T>  callback) {
        if (callback != null) {
            new ParsDataInBackgroundAndReturnInUI<> (callback).execute(getPrefs().getString(key, null));
        }
    }

    private static class ParsDataInBackgroundAndReturnInUI<T>  extends AsyncTask<String, Void, T>{
        GetDataCallback<T>  callback;

        ParsDataInBackgroundAndReturnInUI(GetDataCallback<T>  callback) {
            this.callback = callback;
        }

        @Override
        protected T doInBackground(String... strings) {
            return callback.parseData(strings[0]);
        }

        @Override
        protected void onPostExecute(T s) {
            super.onPostExecute(s);
            callback.onData(s);
        }
    }

    @Override
    public String getString(String key, GetDataCallback<String> callback) {
        if (callback != null) {
            callback.onData(getPrefs().getString(key, null));
        }else {
            return getPrefs().getString(key, null);
        }
        return null;
    }

    @Override
    public boolean getBoolean(String key, GetDataCallback<Boolean> callback) {
        if (callback != null) {
            callback.onData(getPrefs().getBoolean(key, false));
        }else{
            return getPrefs().getBoolean(key, false);
        }
        return false;
    }

    @Override
    public int getInt(String key, GetDataCallback<Integer> callback) {
        if (callback != null) {
            callback.onData(getPrefs().getInt(key, 0));
        }else{
            return getPrefs().getInt(key, 0);
        }
        return 0;
    }

    @Override
    public long getLong(String key, GetDataCallback<Long> callback) {
        if (callback != null) {
            callback.onData(getPrefs().getLong(key, 0));
        }else {
            return getPrefs().getLong(key, 0);
        }
        return 0;
    }

    @Override
    public float getFloat(String key, GetDataCallback<Float> callback) {
        if (callback != null) {
            callback.onData(getPrefs().getFloat(key, 0));
        }else {
            return getPrefs().getFloat(key, 0);
        }
        return 0;
    }

    private void addToUserKeys(String key, boolean isUser) {
        Set<String> keys = isUser ? getUserKeys() : getMainKeys();
        if (keys.add(key)) {
            getEditor().putStringSet(isUser ? KEYS_USER : KEYS_MAIN, keys).apply();
        }
    }

    @NonNull
    private Set<String> getUserKeys() {
        return getPrefs().getStringSet(KEYS_USER, new HashSet<String>());
    }

    @NonNull
    private Set<String> getMainKeys() {
        return getPrefs().getStringSet(KEYS_MAIN, new HashSet<String>());
    }

//    static void saveCredentials(String email, String token) {
//        SharedPreferences.Editor mEditor = getEditor();
//        mEditor.putString(PREF_TOKEN, token);
//        mEditor.putString(PREF_MAIL, email);
//        mEditor.apply();
//    }
//
//    static String[] getCredentials() {
//        SharedPreferences mSharedPreferences = getPrefs();
//        return new String[]{mSharedPreferences.getString(PREF_MAIL, null)
//                , mSharedPreferences.getString(PREF_TOKEN, null)};
//    }


    public static void saveToken(String token) {
        getEditor().putString("token", token).apply();
    }

    public static String getToken() {
        return getPrefs().getString("token",null);
    }
    @Override
    public void logout() {
        Set<String> keys = getUserKeys();
        SharedPreferences.Editor editor = getEditor();
        for (String key : keys) {
            editor.remove(key);
        }
        editor.commit();
    }

    @Override
    public void clearData() {
        SharedPreferences.Editor editor = getEditor();

        Set<String> keys = getUserKeys();
        for (String key : keys) {
            editor.remove(key);
        }

        keys = getMainKeys();
        for (String key : keys) {
            editor.remove(key);
        }

        editor.commit();
    }

//    this below do not remove on logoutOnServer

    @Override
    public String getDeviceId() {
        SharedPreferences mSharedPreferences = getPrefs();
        String token = mSharedPreferences.getString("device", null);
        if (token == null) {
            token = UUID.randomUUID().toString();
            SharedPreferences.Editor editor = mSharedPreferences.edit();
            editor.putString("device", token);
            editor.apply();
        }
        return token;
    }


}
