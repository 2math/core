package com.futurist_labs.android.base_library.repository.persistence;

import androidx.annotation.WorkerThread;

/**
 * Created by Galeen on 5/21/18.
 */
public interface SetDataCallback<T>{
    /**
     * This method will be called in background and will be needed only for Managers that need to convert the object to String(json)
     * @param data object it self
     * @return json to be saved
     */
    @WorkerThread
    String parseData(T data);
}
