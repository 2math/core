package com.futurist_labs.android.base_library.model;

import android.content.DialogInterface;

/**
 * Created by Galeen on 12/22/2017.
 */

public class InfoMessage {
    private String message;
    private int messageResource;
    private DialogInterface.OnClickListener listener;

    public InfoMessage(String message, int messageResource, DialogInterface.OnClickListener listener) {
        this.message = message;
        this.messageResource = messageResource;
        this.listener = listener;
    }

    public InfoMessage(String message) {
        this.message = message;
    }

    public InfoMessage(int messageResource) {
        this.messageResource = messageResource;
    }

    public String getMessage() {
        return message;
    }

    public int getMessageResource() {
        return messageResource;
    }

    public DialogInterface.OnClickListener getListener() {
        return listener;
    }
}
