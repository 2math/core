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
import com.futurist_labs.android.base_library.utils.LogUtils;

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
    private WeakReference<Callback> callbackWeakReference;

    public void checkVersions(AppCompatActivity activity, BaseEvents presenterCallback, Action action,
                              int progressType, final int currentAppVersion, Callback callback) {
        if (activity == null || action == null) {
            return;
        }
        activityWeakReference = new WeakReference<>(activity);
        callbackWeakReference = new WeakReference<>(callback);

        BaseNetworkManager.doMainActionSynchronized(
                new MainCallback<Versions>(presenterCallback, progressType) {
                    @Override
                    public void onNoNetwork() {
                        super.onNoNetwork();
                        if (callbackWeakReference.get() != null) {
                            callbackWeakReference.get().onEnd(-2);
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
                        if (activityWeakReference.get() != null) {
                            int result = validateVersionsAndAct(activityWeakReference.get(),
                                                                networkResponse.object, currentAppVersion);
                            if (callbackWeakReference.get() != null) {
                                callbackWeakReference.get().onEnd(result);
                            }
                        }
                    }

                    @Override
                    public void onError(String msg, NetworkResponse networkResponse) {
                        super.onError(msg, networkResponse);
                        if (callbackWeakReference.get() != null) {
                            callbackWeakReference.get().onEnd(-1);
                        }
                    }
                },
                action,
                "checkVersions");
    }

    public interface Callback {
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
//        versionsFromServer.setCurrentVersion(4);
//        versionsFromServer.setMinimalVersion(3);
        LogUtils.d("CurrentVersion : " + versionsFromServer.getCurrentVersion()
                   + "  MinimalVersion: " + versionsFromServer.getMinimalVersion());
        int action = validateVersions(versionsFromServer, currentVersion);
        actOnNewVersions(activity, action);
        return action;
    }

    public int validateVersions(Versions versionsFromServer, int currentVersion) {
        if (versionsFromServer == null) {
            return ACTION_NO_VERSIONS;
        } else {
            if (versionsFromServer.getCurrentVersion() > currentVersion) {
                if (versionsFromServer.getMinimalVersion() > currentVersion) {
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
        if (activity == null) {
            return;
        }

        if (action == ACTION_MUST_UPDATE) {
            UpdateActivity.show(activity);
        } else if (action == ACTION_CAN_UPDATE) {
            UpdateDialogFragment dialogFragment = UpdateDialogFragment.newInstance(null);
            dialogFragment.show(activity.getSupportFragmentManager(), "UpdateDialogFragment");
        }
    }
}
