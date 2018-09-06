package com.futurist_labs.android.base_library.repository.network;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.futurist_labs.android.base_library.R;
import com.futurist_labs.android.base_library.model.ServerError;
import com.futurist_labs.android.base_library.ui.BaseEvents;
import com.futurist_labs.android.base_library.utils.LogUtils;

import java.lang.ref.WeakReference;


/**
 * Created by Galeen on 21.1.2016 г..
 * Implements NetworkOperationCallback, it has all the logic for
 * show/hide progress, error , noNetwork etc.
 * Each caller(ViewModel presenter) can create a inner class which can extend MainCallback and
 * override its methods usually onPostExecute(when is succeeded), onError(when server return 300+)
 * and inTheEndOfDoInBackground(which is called when response is positive right before onPostExecute and is in the
 * background, so you can parse and persist your data with your Repository)
 * Keeps the presenterCallback as WeakReference.
 */
public class MainCallback<T> implements NetworkOperationCallback<T> {
    public static final int TYPE_DIALOG = 273;
    public static final int TYPE_BAR = 272;
    public static final int TYPE_NO_PROGRESS = 271;
    public WeakReference<BaseEvents> presenterCallback;
    private boolean withProgress, cancelable = false, isTaskRunning = false, mayInterruptIfRunning = false;
    private int progressType;
    private String loadingMsg;
    private AsyncTask task;
    public int id;

    /**
     * @param presenterCallback extend BaseActivity which will make sure to show/hide progress
     * @param progressType      should be one of MainCallback.TYPE_DIALOG , MainCallback.TYPE_BAR or MainCallback.TYPE_NO_PROGRESS
     */
    public MainCallback(BaseEvents presenterCallback, int progressType) {
        this(presenterCallback, progressType, false, null);
    }

    public MainCallback(BaseEvents presenterCallback, int progressType, boolean cancelable, String loadingMsg) {
        this.progressType = progressType;
        this.cancelable = cancelable;
        this.loadingMsg = loadingMsg;
        withProgress = progressType != TYPE_NO_PROGRESS;
        this.presenterCallback = new WeakReference<>(presenterCallback);
    }

    /**
     * Will call BaseEvents.showNoNetwork()
     * and remove it self from it with removeCallback(this)
     * indicate that task is not running any more
     */
    @Override
    public void onNoNetwork() {
        if (checkPresenterIsAlive()) {
            presenterCallback.get().showNoNetwork();
        }
        removeCallback();
        isTaskRunning = false;
    }

    /**
     * Will show a progress if progress option was set.
     * Indicates that task is running now.
     * Passes it self to BaseEvents, to have a reference for it
     * so BaseEvents can stop it on any time,if is from BaseViewModel
     * will be cleared when onCleared() is called on it.
     */
    @Override
    public void onPreExecute() {
        if (withProgress) {
            showProgress();
        }
        isTaskRunning = true;
        if (presenterCallback.get() != null) {
            presenterCallback.get().addCallback(this);
        }
    }

    @Override
    public void inTheEndOfDoInBackground(NetworkResponse networkResponse) {

    }

    /**
     * Hides progress if was set.
     * Removes it self from BaseEvents as callback
     * indicates that task is not running anymore
     *
     * @param networkResponse return
     */
    @Override
    public void onPostExecute(NetworkResponse<T> networkResponse) {
        if (withProgress) {
            hideProgress();
        }
        removeCallback();
        isTaskRunning = false;
    }

    @Override
    public void onProgress(int progress) {

    }

    /**
     * Hides progress if was set.
     * Removes it self from BaseEvents as callback
     * indicates that task is not running anymore
     */
    @Override
    public void onCancel() {
        if (withProgress) {
            hideProgress();
        }
        removeCallback();
        isTaskRunning = false;
    }

    /**
     * Hides progress if was set.
     * Removes it self from BaseEvents as callback.
     * Indicates that task is not running anymore.
     * Check for main errors and sends them to BaseEvents.showError(msg, networkResponse).
     * If you override it and make custom handling before to call super use checkCustomError.
     *
     * If you don't want to show error message set NetworkResponse.withoutErrorCheck = true before to call super.
     *
     * If you don't want to call super must call onErrorEnd() instead!
     *
     * Logs error message
     *
     * @param networkResponse return
     */
    @Override
    public void onError(String msg, NetworkResponse networkResponse) {
        logError(msg, networkResponse);

        showError(msg, networkResponse);
        onErrorEnd();
    }


    @Override
    public void onOldData(NetworkResponse networkResponse) {

    }

    protected void showError(String msg, NetworkResponse networkResponse) {
        if (withProgress) {
            hideProgress();
        }
        if (checkPresenterIsAlive() && !networkResponse.withoutErrorCheck) {
            checkError(msg, networkResponse);
            presenterCallback.get().showError(msg, networkResponse);
        }
    }

    /**
     * Pass custom errors and messages for them and the message will be set on networkResponse.errorMsg
     * which is used from BaseUi to show an error
     *
     * @param msg             if not null will be used for error
     * @param networkResponse if not null has no errorMsg and has ServerError will be precessed, if has errorMsg
     * @param errors          array with possible errors non null
     * @param messages        array with messages for each error from errors array, positions are important
     */
    protected boolean checkCustomError(String msg, NetworkResponse networkResponse, @NonNull String[] errors,
                                       @NonNull @StringRes int[] messages) {
        if (networkResponse != null && networkResponse.errorMsg == 0 && msg == null
                && networkResponse.serverError != null && networkResponse.serverError.getCode() != null
                && errors.length == messages.length) {
            int size = errors.length;
            for (int i = 0; i < size; i++) {
                if (networkResponse.serverError.getCode().equalsIgnoreCase(errors[i])) {
                    networkResponse.errorMsg = messages[i];
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * if you override onError and don't want to call super
     * call this method at the end
     */
    public void onErrorEnd() {
        removeCallback();
        isTaskRunning = false;
    }

    public static void logError(String msg, NetworkResponse networkResponse) {
        if (networkResponse != null)
            LogUtils.error("Error", "url : " + networkResponse.url +
                    "\nmessage : " + msg +
                    "\nresponse : " + networkResponse.responseCode +
                    "\n" + networkResponse.json +
                    "\n" + networkResponse.error);
        else
            LogUtils.error("Error", "message : " + msg);
    }


    /**
     * Cancel the operation, instead of onPostExecute , onCancel will be notified
     * <p>
     * If you have set on this callback mayInterruptIfRunning True then the thread executing this task should be interrupted; otherwise, in-progress tasks are allowed to complete.
     * If you want to let the service to get the data from the service and pass it to inTheEndOfDoInBackground use false.
     *
     * @return False if the task could not be cancelled, typically because it has already completed normally; true otherwise
     */
    public boolean cancelBackgroundOperation() {
        return task != null && task.cancel(mayInterruptIfRunning);
    }

    private boolean checkPresenterIsAlive() {
        return presenterCallback.get() != null;// && !presenterCallback.get().isFinishing();
    }

    private void showProgress() {
        if (checkPresenterIsAlive()) {
            switch (progressType) {
                case TYPE_DIALOG:
                    showStaticProgress();
                    break;
                case TYPE_BAR:
                    presenterCallback.get().showProgressBar();
                    break;
            }
        }
    }

    private void hideProgress() {
        if (checkPresenterIsAlive()) {
            switch (progressType) {
                case TYPE_DIALOG:
                    hideStaticProgress();
                    break;
                case TYPE_BAR:
                    presenterCallback.get().hideProgressBar();
                    break;
            }
        }
    }

    protected void hideStaticProgress() {
        presenterCallback.get().hideProgressDialog();
    }

    protected void showStaticProgress() {
        if (cancelable) {
            presenterCallback.get().showProgressDialog(loadingMsg, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialogInterface) {
                    cancelBackgroundOperation();
                }
            });
        } else {
            presenterCallback.get().showProgressDialog(loadingMsg, null);
        }
    }

    // TODO: 6/13/2018 logic for main errors
    private void checkError(String msg, NetworkResponse networkResponse) {
        if (networkResponse != null && networkResponse.errorMsg == 0 && msg == null && networkResponse.serverError != null) {
            if (networkResponse.serverError.getCode() != null) {
                switch (networkResponse.serverError.getCode()) {
                    case ServerError.ACCESS_DENIED:
                        networkResponse.errorMsg = R.string.err_access_denied;
                        break;
                    case ServerError.CONSTRAINT_VIOLATION:
                        networkResponse.errorMsg = R.string.err_constraint_violation;
                        break;
                    case ServerError.DUPLICATE_DATA:
                        networkResponse.errorMsg = R.string.err_duplicate_data;
                        break;
                    case ServerError.INVALID_JSON:
                        networkResponse.errorMsg = R.string.err_invalid_json;
                        break;
                    case ServerError.INVALID_REQUEST_FORMAT:
                        networkResponse.errorMsg = R.string.err_invalid_request_format;
                        break;
                    case ServerError.MISSING:
                        networkResponse.errorMsg = R.string.err_missing;
                        break;
                    case ServerError.REQUEST_VALIDATION:
                        networkResponse.errorMsg = R.string.err_request_validation;
                        break;
                    case ServerError.UNKNOWN:
                        networkResponse.errorMsg = R.string.err_unknown;
                        break;
                    case ServerError.RESOURCE_NOT_FOUND:
                        networkResponse.errorMsg = R.string.err_resource_not_found;
                        break;
                    case ServerError.USER_UPDATE_FAILED:
                        networkResponse.errorMsg = R.string.err_user_update_failed;
                        break;
                    case ServerError.DUPLICATION_NOT_ALLOWED:
                        networkResponse.errorMsg = R.string.err_duplication_not_allowed;
                        break;
                    case ServerError.TOO_YOUNG_FOR_REGISTRATION:
                        networkResponse.errorMsg = R.string.err_too_young_for_registration;
                        break;
                    case ServerError.LOCATION_NOT_SUPPORTED:
                        networkResponse.errorMsg = R.string.err_location_not_supported;
                        break;
                    case ServerError.PATIENT_YOUNGER_THAN_AN_ADULT:
                        networkResponse.errorMsg = R.string.err_patient_younger_than_an_adult;
                        break;
                    case ServerError.PATIENT_OLDER_THAN_A_CHILD:
                        networkResponse.errorMsg = R.string.err_patient_older_than_a_child;
                        break;
                    case ServerError.ADULT_PATIENT_WITH_MONTHS:
                        networkResponse.errorMsg = R.string.err_adult_patient_with_months;
                        break;
                    default:
                        networkResponse.errorMsg = R.string.msg_main_server_error;
                }
            } else {
                if (networkResponse.serverError.getText() != null) {
                    //there was a problem parsing the error and here is the text send from the server
                }
            }
        }
    }

    /**
     * removes this callback from presenterCallback's callbacks set as is done
     */
    private void removeCallback() {
        if (checkPresenterIsAlive()) {
            presenterCallback.get().removeCallback(this);
        }
    }

    void setTask(AsyncTask task) {
        this.task = task;
    }

    public boolean isTaskRunning() {
        return isTaskRunning;
    }

    public boolean isMayInterruptIfRunning() {
        return mayInterruptIfRunning;
    }

    public void setMayInterruptIfRunning(boolean mayInterruptIfRunning) {
        this.mayInterruptIfRunning = mayInterruptIfRunning;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MainCallback)) return false;

        MainCallback that = (MainCallback) o;

        return id == that.id;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
