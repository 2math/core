package com.futurist_labs.android.base_library.repository.network;

/**
 * Created by Galeen on 18.1.2016 г..
 * callback for network operations
 */
public interface NetworkOperationCallback<T> {
    /**
     * Available network check will be done first and if false
     * this callback will be updated.
     * Operation ends after this call.
     * called on UI
     */
    void onNoNetwork();

    /**
     * Same as AsyncTask.onPreExecute
     * We have network and operation starts, still called on UI
     */
    void onPreExecute();

    /**
     * This method is called before End Of DoInBackground
     * and here you can even manipulate the response.
     * Main usage is to use the background thread to save some data
     * from the response or parse it.
     * It will be called only if there is no error and the response code is <300.
     * <p>
     * Here you have the response, you can parse it to the object you want and even persist it in your DB.
     * Usually parsed object is passed in the NetworkResponse.object or object2, then this NetworkResponse
     * is passed to onPostExecute(NetworkResponse<T> networkResponse) which is on the UI thread
     * and you can resend it to the UI listener.
     * <p>
     * This interface is generic and with T we are pointing what type we will convert our response data to
     * pass it to onPostExecute. Basically T is the type of NetworkResponse.object
     * <p>
     * If you want to chain calls here you can also call more request as ServerOperation.doServerCall(..),
     * which is executed on same thread
     *
     * @param networkResponse will be returned to onPostExecute
     */
    void inTheEndOfDoInBackground(NetworkResponse networkResponse);

    /**
     * Same as AsyncTask.onPostExecute
     * Operation is completed and is successful(response code <300).
     * Usually in networkResponse.object or object2 we have parsed
     * our model object from the response, which was done in inTheEndOfDoInBackground.
     * This way here we don't parse or persist we only redirect.
     * called on UI
     */
    void onPostExecute(NetworkResponse<T> networkResponse);

    /**
     * Same as onPostExecute but with more meaningful name, onPostExecute calls
     * this method and is same thing if you override any of them, but is better to use this one.
     *
     * Operation is completed and is successful(response code <300).
     * Usually in networkResponse.object or object2 we have parsed
     * our model object from the response, which was done in inTheEndOfDoInBackground.
     * This way here we don't parse or persist we only redirect.
     * called on UI
     */
    void onSuccess(NetworkResponse<T> networkResponse);

    /**
     * Same as AsyncTask.onProgress
     * Currently not in use
     * called on UI
     */
    void onProgress(int progress);

    /**
     * Runs on the UI thread after
     * task.cancel(boolean) is invoked and doInBackground(Object[]) has finished.
     */
    void onCancel();

    /**
     * Same as AsyncTask.onPostExecute
     * Operation is completed and is unsuccessful(response code >=300).
     * Here we may have an error message from ServerOperation or in the
     * networkResponse[String error, int errorMsg, ServerError serverError]
     * called on UI
     */
    void onError(String msg, NetworkResponse networkResponse);

    /**
     * Currently not in use
     * @param networkResponse return
     */
    void onOldData(NetworkResponse networkResponse);

}
