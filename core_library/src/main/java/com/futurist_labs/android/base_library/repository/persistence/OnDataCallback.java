package com.futurist_labs.android.base_library.repository.persistence;

/**
 * Created by Galeen on 6/19/2018.
 */
public interface OnDataCallback<T> {
    /**
     * Called when the data is changed.
     *
     * @param t The new data
     */
    void onData(T t);
}
