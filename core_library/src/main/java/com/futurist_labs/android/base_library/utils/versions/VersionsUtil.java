package com.futurist_labs.android.base_library.utils.versions;

import android.support.v7.app.AppCompatActivity;

import com.futurist_labs.android.base_library.repository.network.Action;
import com.futurist_labs.android.base_library.repository.network.BaseNetworkManager;
import com.futurist_labs.android.base_library.repository.network.MainCallback;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.repository.persistence.BaseJsonParser;
import com.futurist_labs.android.base_library.ui.BaseEvents;
import com.futurist_labs.android.base_library.ui.versions.UpdateActivity;
import com.futurist_labs.android.base_library.ui.versions.UpdateDialogFragment;

import java.lang.ref.WeakReference;

/**
 * Created by Galeen on 8/27/2019.
 */
public class VersionsUtil {
    public static final int ACTION_NO_VERSIONS = 0;
    public static final int ACTION_OK = 1;
    public static final int ACTION_CAN_UPDATE = 2;
    public static final int ACTION_MUST_UPDATE = 3;

    private WeakReference<AppCompatActivity> activityWeakReference;

    public void checkVersions(AppCompatActivity activity, BaseEvents presenterCallback, Action action,
                              int progressType, final int currentAppVersion, final Callback callback) {
        if (activity == null || action == null) {
            return;
        }
        activityWeakReference = new WeakReference<>(activity);
        BaseNetworkManager.doMainActionSynchronized(
                new MainCallback<Versions>(presenterCallback, progressType) {
                    @Override
                    public void onNoNetwork() {
                        super.onNoNetwork();
                        if(callback!=null){
                            callback.onEnd(-2);
                        }
                    }

                    @Override
                    public void inTheEndOfDoInBackground(NetworkResponse networkResponse) {
                        super.inTheEndOfDoInBackground(networkResponse);
                        networkResponse.object = BaseJsonParser.fromJson(networkResponse.json,
                                Versions.class);
                    }

                    @Override
                    public void onSuccess(NetworkResponse<Versions> networkResponse) {
                        super.onSuccess(networkResponse);
                        if(activityWeakReference.get() != null){
                            int result = validateVersionsAndAct(activityWeakReference.get(),
                                    networkResponse.object,currentAppVersion);
                            if(callback!=null){
                                callback.onEnd(result);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg, NetworkResponse networkResponse) {
                        super.onError(msg, networkResponse);
                        if(callback!=null){
                            callback.onEnd(-1);
                        }
                    }
                },
               action,
                "checkVersions");
    }

    public interface Callback{
        void onEnd(int status);
    }
    /**
     * Will check versions with current and show blocking activity or update dialog
     *
     * @param activity           calling activity
     * @param versionsFromServer downloaded versions
     * @param currentVersion     currentBuildVersion
     * @return action
     */
    public int validateVersionsAndAct(AppCompatActivity activity, Versions versionsFromServer,
                                      int currentVersion) {
        int action = validateVersions(versionsFromServer, currentVersion);
        actOnNewVersions(activity, action);
        return action;
    }

    public int validateVersions(Versions versionsFromServer, int currentVersion) {
        if (versionsFromServer == null) {
            return ACTION_NO_VERSIONS;
        } else {
            if (versionsFromServer.getCurrentAndroidVersion() > currentVersion) {
                if (versionsFromServer.getMinimumAndroidVersion() > currentVersion) {
                    return ACTION_MUST_UPDATE;
                } else {
                    return ACTION_CAN_UPDATE;
                }
            } else {
                return ACTION_OK;
            }
        }
    }

    public void actOnNewVersions(AppCompatActivity activity, int action) {
        if (activity == null) return;

        if (action == ACTION_MUST_UPDATE) {
            UpdateActivity.show(activity);
        } else if (action == ACTION_CAN_UPDATE) {
            UpdateDialogFragment dialogFragment = UpdateDialogFragment.newInstance(null);
            dialogFragment.show(activity.getSupportFragmentManager(), "UpdateDialogFragment");
        }
    }
}
