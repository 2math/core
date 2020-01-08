package com.futurist_labs.android.base_library.ui;

import android.content.DialogInterface;

import com.futurist_labs.android.base_library.model.InfoMessage;
import com.futurist_labs.android.base_library.model.ProgressData;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.utils.LogUtils;

import java.util.HashSet;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * Created by Galeen on 12/21/2017.
 * implements LiveData for show/hide progress , errors and info messages
 * Most ViewModel presenters should extend from this one
 * In the app create new AppBasedViewModel which will extend from this one and add some app common logic
 */

public abstract class BaseViewModel extends ViewModel {
    private int currentTaskId = 0;
    public HashSet<MainCallback> callbacks = new HashSet<>();

    private MutableLiveData<ProgressData> showProgressDialog;
    private MutableLiveData<Boolean> showProgressBar;
    private MutableLiveData<Boolean> showNoNetwork;
    private MutableLiveData<NetworkResponse> showError;
    private MutableLiveData<InfoMessage> showMessage;
    private MutableLiveData<NetworkResponse> showErrorDialog;

    protected abstract void init();


    LiveData<ProgressData> shouldShowProgressDialog() {
        if (showProgressDialog == null) {
            showProgressDialog = new MutableLiveData<>();
        }
        return showProgressDialog;
    }

    LiveData<Boolean> shouldShowProgressBar() {
        if (showProgressBar == null) {
            showProgressBar = new MutableLiveData<>();
        }
        return showProgressBar;
    }

    LiveData<NetworkResponse> showErrorDialog() {
        if (showErrorDialog == null) {
            showErrorDialog = new SingleLiveEvent<>();
        }
        return showErrorDialog;
    }

    LiveData<Boolean> shouldShowNoNetwork() {
        if (showNoNetwork == null) {
            showNoNetwork = new MutableLiveData<>();
        }
        return showNoNetwork;
    }

    LiveData<NetworkResponse> shouldShowError() {
        if (showError == null) {
            showError = new SingleLiveEvent<>();
        }
        return showError;
    }

    LiveData<InfoMessage> shouldShowMessage() {
        if (showMessage == null) {
            showMessage = new SingleLiveEvent<>();
        }
        return showMessage;
    }

    public void showError(String msg) {
        if (showError != null) showError.setValue(new NetworkResponse(msg));
    }

    public void showError(int msg) {
        if (showError != null) showError.setValue(new NetworkResponse(msg));
    }

    public void showErrorDialog(int msg) {
        if (showErrorDialog != null) showErrorDialog.setValue(new NetworkResponse(msg));
    }

    public void setShowErrorDialog(String msg) {
        if (showErrorDialog != null) showErrorDialog.setValue(new NetworkResponse(msg));
    }

    public void showMessage(String msg) {
        if (showMessage != null) showMessage.setValue(msg == null ? null : new InfoMessage(msg));
    }

    public void showMessageDialog(String msg, DialogInterface.OnClickListener listener) {
        if (showMessage != null)
            showMessage.setValue(msg == null ? null : new InfoMessage(msg, 0, listener));
    }

    public void showMessage(int msg) {
        if (showMessage != null) showMessage.setValue(new InfoMessage(msg));
    }

    public BaseEvents getNetworkCallback() {
        return baseEvents;
    }

    private BaseEvents baseEvents = new BaseEvents() {
        @Override
        public void removeCallback(MainCallback callback) {
            callbacks.remove(callback);
        }

        @Override
        public void addCallback(MainCallback callback) {
            currentTaskId++;
            callback.id = currentTaskId;
            callbacks.add(callback);
        }

        @Override
        public void hideProgressBar() {
            if (showProgressBar != null) showProgressBar.setValue(false);
        }

        @Override
        public void hideProgressDialog() {
            if (showProgressDialog != null) showProgressDialog.postValue(null);
        }

        @Override
        public void showProgressBar() {
            if (showProgressBar != null) showProgressBar.setValue(true);
        }

        @Override
        public void showProgressDialog(String loadingMsg, DialogInterface.OnCancelListener onCancelListener) {
            if (showProgressDialog != null) {
                showProgressDialog.setValue(new ProgressData(loadingMsg, onCancelListener));
            }
        }

        @Override
        public void showNoNetwork() {
            if (showNoNetwork != null) showNoNetwork.setValue(true);
        }

        @Override
        public void showError(String msg, NetworkResponse networkResponse) {
            if (showError != null) showError.setValue(networkResponse);
        }
    };

    @Override
    protected void onCleared() {
        super.onCleared();
        //stop all background processes
        for (MainCallback callback : callbacks) {
            if (callback != null) {
                callback.cancelBackgroundOperation();
                LogUtils.d(this.getClass().getSimpleName(), "cancelBackgroundOperation");
            }
        }
        callbacks.clear();
    }

    public boolean isSameCallRunning(MainCallback callbackToRun) {
        //check if there is no running call already
        for (MainCallback callback : callbacks) {
            if (callbackToRun.getClass().isInstance(callback)) return true;
        }
        return false;
    }
}
