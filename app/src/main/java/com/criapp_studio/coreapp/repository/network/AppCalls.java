package com.criapp_studio.coreapp.repository.network;

import com.criapp_studio.coreapp.repository.json.AppJsonParser;
import com.futurist_labs.android.base_library.repository.network.Action;

import java.io.IOException;

import androidx.annotation.NonNull;

/**
 * Created by Galeen on 9/5/2018.
 */
public class AppCalls {

    @NonNull
    public static Action getLoginAction(String mail, String pass) throws IOException {
        return new Action(Action.POST, NetConstants.SESSIONS,
                AppJsonParser.writeLogin(mail, pass, false), AppJsonParser.writeLogin(mail, pass, true));
    }
}
