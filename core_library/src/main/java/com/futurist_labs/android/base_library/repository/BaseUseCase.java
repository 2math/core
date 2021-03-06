package com.futurist_labs.android.base_library.repository;

import android.arch.lifecycle.MutableLiveData;

import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.ui.BaseEvents;
import com.futurist_labs.android.base_library.ui.BaseViewModelWithRepository;

/**
 * Created by Galeen on 9/11/2018.
 * Declare it with the return type and Repository type.
 * Should pass observable and viewModel.
 */
public abstract class BaseUseCase<T /*return result for your observable*/,K /*repository interface*/> {
    protected MainCallback callback;
    protected MutableLiveData<T> observable;
    protected BaseViewModelWithRepository<K> viewModel;

    public BaseUseCase(BaseViewModelWithRepository<K> viewModel, MutableLiveData<T> observable) {
        this.observable = observable;
        this.viewModel = viewModel;
    }

    /**
     * If you have no parameters to do your call(just callback) , you can call this method directly.
     * If you have any extra data, is better to create new public method in your UseCase which will hold
     * the parameters sent and must call run() at the end
     */
    public void run() {
        if(isNotRunning()){
            if(callback == null) callback = getCallback();
            doCall();
        }
    }

    /**
     *
     * @return Create a callback to be used in doCall().
     * If you need just simple call/parse/return callback you can use UseCaseSimpleCallback, don't
     * forget to override the method parseResponse(String json) in this case
     */
    protected abstract MainCallback getCallback();

    /**
     * Here you have the Callback created already
     * do your call with it
     */
    protected abstract void doCall();

    /**
     * This method will be called to parse response in background for UseCaseSimpleCallback.
     * If you write your custom MainCallback, no need to override it
     * @param json JSON to parse
     * @return parsed Object
     */
    protected  abstract T parseResponse(String json);

    private boolean isNotRunning() {
        return callback == null || !callback.isTaskRunning();
    }

    protected class UseCaseSimpleCallback extends MainCallback<T> {
        public UseCaseSimpleCallback(BaseEvents presenterCallback, int progressType) {
            super(presenterCallback, progressType);
        }

        @Override
        public void inTheEndOfDoInBackground(NetworkResponse networkResponse) {
            super.inTheEndOfDoInBackground(networkResponse);
            networkResponse.object = parseResponse(networkResponse.json);
        }

        @Override
        public void onSuccess(NetworkResponse<T> networkResponse) {
            super.onSuccess(networkResponse);
            if (observable != null) observable.setValue(networkResponse.object);
        }

        @Override
        public void onError(String msg, NetworkResponse networkResponse) {
            super.onError(msg, networkResponse);
            if (observable != null) observable.setValue(null);
        }
    }
}
