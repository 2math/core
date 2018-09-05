package com.futurist_labs.android.base_library.repository.persistence;

/**
 * Created by Galeen on 5/21/18.
 */
public interface BasePersistenceInterface {
    // TODO: 7/22/18 add insert , delete, deleteAll and find. save object works as update or delete in single object table, same as get object
    /**
     * This function notifies if the PersistenceManager needs cache helper as BaseCache or not
     * @return true - All working data will be kept in BaseCache map, on request will be checked there first
     * false - eny request for data will be from the DB directly
     */
    boolean useCache();

    <T> void save(String key, T value, SetDataCallback callback, boolean isUser);

    void save(String key, String value, boolean isUser);

    void save(String key, boolean value, boolean isUser);

    void save(String key, int value, boolean isUser);

    void save(String key, float value, boolean isUser);

    void save(String key, long value, boolean isUser);

    <T> void get(String key, GetDataCallback<T>  callback);

    String getString(String key, GetDataCallback<String> callback);

    boolean getBoolean(String key, GetDataCallback<Boolean> callback);

    int getInt(String key, GetDataCallback<Integer> callback);

    long getLong(String key, GetDataCallback<Long> callback);

    float getFloat(String key, GetDataCallback<Float> callback);;

    void logout();

    void clearData();

    String getDeviceId();
}
