package com.futurist_labs.android.base_library.repository.network;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

import java.io.File;


/**
 * Created by Galeen on 13.1.2016 г..
 * will do the request according to the Action object send to it and will
 * update the MainCallback on each of it's stages.
 * There is a doServerCall static method which can be called from another background thread.
 */
public class ServerOperation extends AsyncTask<Action, Void, NetworkResponse> {
    private NetworkOperationCallback mCallback = null;
    private String token = null;

    ServerOperation(NetworkOperationCallback mCallback, String token) {
        this.mCallback = mCallback;
        this.token = token;
    }

    public NetworkOperationCallback getmCallback() {
        return mCallback;
    }

    public void setmCallback(NetworkOperationCallback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mCallback != null)
            mCallback.onPreExecute();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        if (mCallback != null)
            mCallback.onCancel();
    }

    @Override
    protected void onCancelled(NetworkResponse networkResponse) {
        super.onCancelled(networkResponse);
        if (mCallback != null)
            mCallback.onCancel();
    }

    @Override
    protected NetworkResponse doInBackground(Action... params) {
//       waitTime(3000);//test for crappy network
        Action action = params[0];
        if (action.priority != Action.NORMAL_PRIORITY) {
            android.os.Process.setThreadPriority(action.priority);
        }
        NetworkResponse res = doServerCall(action, token);

        if (res != null && res.responseCode == 401 && BaseLibraryConfiguration.getInstance().getNetworkAuthoriser() != null) {//session expired
            NetworkResponse res1 = BaseLibraryConfiguration.getInstance().getNetworkAuthoriser().authorise();
            if (res1 != null && res1.isResponsePositive() && res1.object != null) {
                action.isFullUrl = true;
                res = doServerCall(action, (String) res1.object);
            }
        }

        if (res != null && res.isResponsePositive()) {
            if (!action.isCheckServerUrl || (res.url != null && res.url.startsWith(BaseLibraryConfiguration.getInstance().getServerUrl()))) {
                //we can use the url from action too
                if (mCallback != null){
                    mCallback.inTheEndOfDoInBackground(res);
                }
            } else {//not from our server
                res.responseCode = NetworkResponse.ERROR_WRONG_SERVER;
            }
        }

        return res;
    }

//    private NetworkResponse loginAndRepeatCall(Action action, NetworkResponse res) {
//        String[] creds = PersistenceManager.getCredentials();
//        if(TextUtils.isEmpty(creds[0]) || TextUtils.isEmpty(creds[1])){
//            return res;
//        }
//        NetworkResponse res1 = doServerCall(Actions.loginAction(creds), token);
//        if(res1.isResponsePositive()){
//            Session session = MyJsonParser.readSession(res1.json);
//            res1.object = session;
//            PersistenceManager.saveSession(res1);
//            res = doServerCall(action, session.getSessionId());
//        }
//        return res;
//    }

    /**
     * This method is called on the same thread
     *
     * @param action - config object
     * @return - Server Response
     */
    public static NetworkResponse doServerCall(final Action action, String token) {
        NetworkResponse res = null;
        if (action != null) {
            GaleenTracker tracker = new GaleenTracker(action.endpoint);
            try {
                tracker.startTracker();
                if (!action.isFullUrl)
                    action.endpoint = BaseLibraryConfiguration.getInstance().getServerUrl() + action.endpoint;
                tracker.logPrettyJson(action, token);
                switch (action.operation) {
                    case Action.PUT:
                        res = NetworkRequestHelper.sendAuthenticatedPut(action.endpoint, action.body, token);
                        break;
                    case Action.POST:
                        res = NetworkRequestHelper.sendPost(action.endpoint, action.body, token, new NetworkRequestHelper.ServerEvents() {
                            @Override
                            public void beforeToReceiveResponse() {
                                //clear the data to empty memory
                                action.body = null;
                            }
                        }, action.headers);
                        break;
                    case Action.POST_WITH_PARAMS:
                        res = NetworkRequestHelper.sendPostWithParams(action.endpoint, action.body, token, new NetworkRequestHelper.ServerEvents() {
                            @Override
                            public void beforeToReceiveResponse() {
                                //clear the data to empty memory
                                action.body = null;
                            }
                        }, action.headers);
                        break;
                    case Action.DELETE:
                        res = NetworkRequestHelper.sendDelete(action.endpoint, action.body, token);
                        break;
                    case Action.UPLOAD_FILE:
//                        res = BackEndHelper.postFile(action.endpoint, action.file, token, action.params);
//                        res = NetworkRequestHelper.multipartRequest(action.endpoint, action.params, action.file,
//                                "file", getMimeType(action.file), token);
                        res = NetworkRequestHelper.multipartRequest(action.endpoint, action.params, action.files,
                                action.fileFields, token);
                        break;
                    case Action.DOWNLOAD_FILE:
                        res = NetworkRequestHelper.downloadFile(action.endpoint, action.file,token, action.headers);
                        break;
                    case Action.GET:
                        res = NetworkRequestHelper.sendAuthenticatedGet(action.endpoint, token
                                , action.params, action.headers);
                        break;
                    case Action.GET_UNAUTHORIZED:
                        res = NetworkRequestHelper.sendGet(action.endpoint, action.params, action.headers);
                        break;
                    case Action.DELAY_2_MIN:
                        waitTime(1000 * 60 * 2);
                        break;
                }
                tracker.stopTracker();
                tracker.logResponse(res);

            } catch (Exception e) {
                tracker.logError(e, e.toString());
                res = null;
            }
        }
        return res;
    }

    @Override
    protected void onPostExecute(NetworkResponse res) {
        super.onPostExecute(res);
        onFinish(mCallback, res);
    }

    private static void waitTime(int mills) {
        Log.e("waitTime", "start " + System.currentTimeMillis());
        try {
            Thread.sleep(mills);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.e("waitTime", "end " + System.currentTimeMillis() + " for : " + mills);
    }


    private static void onFinish(NetworkOperationCallback mCallback, NetworkResponse res) {
        if (mCallback != null) {
            if (res != null && res.isResponsePositive()) {
//                Error error = MyJsonParser.checkForError(res.json);
//                if (error != null && error.getText() != null) {
//                    mCallback.onError(error.getText(), res);
//                } else
                mCallback.onSuccess(res);
            } else
                mCallback.onError(null, res);
        }
    }

    /**
     * @return The MIME type for the given file.
     */
    static String getMimeType(File file) {
        String extension = getExtension(file.getName());

        if (extension.length() > 0)
            return MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.substring(1));

        return "application/octet-stream";
    }

    private static String getExtension(String uri) {
        if (uri == null) {
            return null;
        }

        int dot = uri.lastIndexOf(".");
        if (dot >= 0) {
            return uri.substring(dot);
        } else {
            // No extension.
            return "";
        }
    }

}