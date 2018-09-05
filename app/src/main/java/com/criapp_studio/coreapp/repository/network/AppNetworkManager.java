package com.criapp_studio.coreapp.repository.network;

import com.futurist_labs.android.base_library.repository.network.Action;
import com.futurist_labs.android.base_library.repository.network.BaseNetworkManager;
import com.futurist_labs.android.base_library.repository.network.MainCallback;

import java.io.IOException;

/**
 * Created by Galeen on 9/5/2018.
 */
public class AppNetworkManager {

    public static void login(MainCallback mCallback, String mail, String pass) {
        try {
            Action action = AppCalls.getLoginAction(mail, pass);
            BaseNetworkManager.doMainActionSynchronized(mCallback, action, "login ");
        } catch (IOException e) {
            e.printStackTrace();
            if(mCallback!=null){
                mCallback.onError("login call : parsing error", null);
            }
        }
    }
}
