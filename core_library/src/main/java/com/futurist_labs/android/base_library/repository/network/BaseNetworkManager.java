package com.futurist_labs.android.base_library.repository.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.repository.persistence.BasePersistenceManager;
import com.futurist_labs.android.base_library.utils.LogUtils;


/**
 * Created by Galeen on 18.1.2016 г..
 * It has public static methods which correspond to each server service.
 * They accept a MainCallback(to talk to the caller) and data to send.
 * The NetworkManager creates/parses the data and constructs an Action object which holds all
 * the information for the request, than according to the request is send to the ServerOperation(AsyncTask)
 * as parallel or synchronized request. Before to send it to the ServerOperation it checks for
 * internet connection and if hasn't will notify the MainCallback.onNoNetwork for that and stop the process.
 */
public class BaseNetworkManager {

    public static AsyncTask<Action, Void, NetworkResponse> doMainActionParallel(MainCallback mCallback, Action action, String log) {
        return doMainAction(mCallback, action, log, true, BasePersistenceManager.getToken());

    }

    public static AsyncTask<Action, Void, NetworkResponse> doMainActionSynchronized(MainCallback mCallback, Action action, String log) {
        return doMainAction(mCallback, action, log, false,BasePersistenceManager.getToken());

    }

    private static AsyncTask<Action, Void, NetworkResponse> doMainAction(MainCallback mCallback, Action action, String log, boolean concurrent, String token) {
        LogUtils.net("NetworkManager", (concurrent ? "Parallel " : "Synchronized ") + log + " ...");
        if (isNetworkAvailable()) {
            AsyncTask<Action, Void, NetworkResponse> task;
            if (concurrent) {
                task = new ServerOperation(mCallback, token).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, action);
            } else {
                task = new ServerOperation(mCallback, token).execute(action);
            }
            if(mCallback!=null){
                mCallback.setTask(task);
            }
            return task;
        } else {
            if (mCallback != null) mCallback.onNoNetwork();
            return null;
        }
    }

    public static NetworkResponse doMainActionOnSameThread(Action action, String log) {
        LogUtils.d("NetworkManager ", log + " ...");
        return ServerOperation.doServerCall(action, BasePersistenceManager.getToken());
    }


    public static boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) BaseLibraryConfiguration.getInstance().getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = null;
        if (connectivityManager != null) {
            activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

