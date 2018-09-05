package com.futurist_labs.android.base_library.repository.persistence;

import android.support.annotation.UiThread;
import android.support.annotation.WorkerThread;

/**
 * Created by Galeen on 5/21/18.
 */
public abstract class GetDataCallback<T> {
    /**
     * This method will be called on UI thread
     * @param t response
     */
    @UiThread
    public abstract void onData(T t);

    /**
     * This method will be called in background and will be needed only for Managers that need to convert the String(json) to Object
     * @param dataToParse json to be parsed
     * @return the Object
     */
    @WorkerThread
    public T parseData(String dataToParse){
        return null;
    }

    // TODO: 7/22/18 create a method for Room with BaseQuery - operation,params
}
