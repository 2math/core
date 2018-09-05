package com.futurist_labs.android.base_library.repository.persistence;

import android.os.AsyncTask;

/**
 * Created by Galeen on 12/21/2017.
 * Helper Task to serialize data
 */

public class SerializeTask<T> extends AsyncTask<Object, Void, T> {
    private ReturnObject<T> returnObject;
    private DoObject<T> doObject;

    public SerializeTask(DoObject<T> doObject, ReturnObject<T> returnObject) {
        this.returnObject = returnObject;
        this.doObject = doObject;
    }

    @Override
    protected T doInBackground(Object... voids) {
        if(doObject!=null) {
            return doObject.execute();
        }else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(T t) {
        super.onPostExecute(t);
        if (returnObject != null) returnObject.onResult(t);
    }


    public interface ReturnObject<T> {
        void onResult(T object);
    }

    public interface DoObject<T> {
        T execute();
    }
}
