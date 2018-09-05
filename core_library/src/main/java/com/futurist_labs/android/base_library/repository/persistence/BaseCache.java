package com.futurist_labs.android.base_library.repository.persistence;


import java.util.HashMap;

/**
 * Created by Galeen on 28.9.2016 г..
 * Singleton object to keep the cache data
 */
class BaseCache {
    private static BaseCache instance;
    //user data
    private HashMap<String, Object> userCache = new HashMap<>();

    //common app data
    private HashMap<String, Object> appCache = new HashMap<>();

    private BaseCache() {
    }

    static BaseCache getInstance() {
        if (instance == null)
            instance = new BaseCache();
        return instance;
    }

    /**
     * Delete all cache
     */
    void clearCache() {
        instance = null;
    }

    /**
     * Delete cache only for current user
     */
    void clearUserCache() {
        userCache.clear();
    }

    /**
     * Store data that is related to the app and not any user
     *
     * @param key  constant
     * @param data actual data
     */
    void inUserData(String key, Object data) {
        userCache.put(key, data);
    }

    Object getFromUser(String key) {
        return userCache.get(key);
    }

    /**
     * Store data that is related to current user
     *
     * @param key  constant
     * @param data actual data
     */
    void inAppData(String key, Object data) {
        appCache.put(key, data);
    }

    Object getFromApp(String key) {
        return appCache.get(key);
    }

    Object get(String key, boolean isUser) {
        return isUser ? userCache.get(key) : appCache.get(key);
    }
}


