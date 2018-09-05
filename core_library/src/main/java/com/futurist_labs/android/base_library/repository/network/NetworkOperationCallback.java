package com.futurist_labs.android.base_library.repository.network;

/**
 * Created by Galeen on 18.1.2016 г..
 * callback for network operations
 */
public interface NetworkOperationCallback<T> {
	 void onPreExecute();

	 void onPostExecute(NetworkResponse<T> networkResponse);

	 void onProgress(int progress);

	 void onCancel();

	 void onError(String msg, NetworkResponse networkResponse);

     void onOldData(NetworkResponse networkResponse);

     void onNoNetwork();

    /**
     * This method can be called before End Of DoInBackground
     * an here you can even manipulate the response
     * Main usage is to use the background thread to save some data
     * from response or parse.It will be called only if there is no error.
     * If you want to chain calls here you can also save call next request.
     * @param networkResponse
     */
    void inTheEndOfDoInBackground(NetworkResponse networkResponse);
}
