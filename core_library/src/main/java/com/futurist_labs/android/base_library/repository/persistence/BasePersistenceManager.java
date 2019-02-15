package com.futurist_labs.android.base_library.repository.persistence;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

/**
 * Created by Galeen on 28.9.2016 г..
 * DB manager keeping data in cache and in DB.
 * When data is requested first checks the cache and if is null then
 * returns from the DB. This usually happen on cold start or if the app was killed by the OS and restored.
 * Currently for DB is used SharedPreference which holds the objects coming from server as JSON.
 * If the app will need deeper DB logic SharedPreferenceManager should be replaced by other DbManager. TBI DBFactory provider
 */
public class BasePersistenceManager {
    public static String getToken() {
        return BaseSharedPreferenceManager.getToken();
    }

    public static void saveToken(String token) {
        BaseSharedPreferenceManager.saveToken(token);
    }


    public static void logout() {
        BaseCache.getInstance().clearUserCache();
        getPersistenceManager().logout();
        BaseSharedPreferenceManager.saveToken(null);
    }

    private static BasePersistenceInterface getPersistenceManager() {
        return BaseLibraryConfiguration.getInstance().getPersistenceManager();
    }

    public static <T> void addValue(String key, T value, SetDataCallback callback) {
        addValue(key, value, callback, true);
    }

    public static <T> void addValue(String key, T value, SetDataCallback callback, boolean isUser) {
        if (getPersistenceManager().useCache()) {
            addDataToCache(key, value, isUser);
        }
        getPersistenceManager().save(key, value, callback, isUser);
    }

    private static <T> void addDataToCache(String key, T value, boolean isUser) {
        if (isUser) {
            BaseCache.getInstance().inUserData(key, value);
        } else {
            BaseCache.getInstance().inAppData(key, value);
        }
    }

    public static void addStringValue(String key, String value) {
        addStringValue(key, value, true);
    }

    public static void addStringValue(String key, String value, boolean isUser) {
        if (getPersistenceManager().useCache()) {
            addDataToCache(key, value, isUser);
        }
        getPersistenceManager().save(key, value, isUser);
    }

    public static void addBooleanValue(String key, boolean value, boolean isUser) {
        addBooleanValue(key, value, isUser, false);
    }

    public static void addBooleanValue(String key, boolean value, boolean isUser, boolean justCache) {
        if (getPersistenceManager().useCache()) {
            addDataToCache(key, value, isUser);
        }
        getPersistenceManager().save(key, value, isUser);
    }

    public static boolean getBoolean(String key, GetDataCallback<Boolean> callback, boolean isUser) {
        if (getPersistenceManager().useCache()) {
            Boolean value = (Boolean) BaseCache.getInstance().get(key, isUser);
            if (value != null) {
                if (callback != null) {
                    callback.onData(value);
                } else {
                    return value;
                }
            }
        }
        return getPersistenceManager().getBoolean(key, callback);
    }

    public static <T> void get(String key, GetDataCallback<T> callback) {
        get(key, callback, true);
    }

    public static <T> void get(final String key, final GetDataCallback<T> callback, final boolean isUser) {
        if (getPersistenceManager().useCache()) {
            Object value = BaseCache.getInstance().get(key, isUser);
            if (value != null && callback != null) {
                callback.onData((T) value);
                return;
            }
        }

        getPersistenceManager().get(key, new GetDataCallback<T>() {
            @Override
            public void onData(T t) {
                if (getPersistenceManager().useCache()) {
                    addDataToCache(key, t, isUser);
                    if (callback != null) callback.onData(t);
                }
            }

            @Override
            public T parseData(String dataToParse) {
                if (callback != null) return callback.parseData(dataToParse);
                return null;
            }
        });
    }

    public static String getString(String key, GetDataCallback<String> callback) {
        return getString(key, callback, true);
    }

    public static String getString(String key, GetDataCallback<String> callback, boolean isUser) {
        if (getPersistenceManager().useCache()) {
            String value = (String) BaseCache.getInstance().get(key, isUser);
            if (value != null) {
                if (callback != null) {
                    callback.onData(value);
                } else {
                    return value;
                }
            }
        }
        return getPersistenceManager().getString(key, callback);
    }

    public static void clearUserCache() {
        BaseCache.getInstance().clearUserCache();
    }

    public static void clearCache() {
        BaseCache.getInstance().clearCache();
    }

}
