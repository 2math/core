package com.futurist_labs.android.base_library.repository.network;

import android.support.annotation.StringRes;

import com.futurist_labs.android.base_library.model.ServerError;

/**
 * Created by Galeen on 18.1.2016 г..
 * This is a data holder created from NetworkRequestHelper on server response.
 * It keeps the responseCode and json/error from the server.
 * It has 2 objects that are used as a Tags to send the parsed object
 * from inTheEndOfDoInBackground to onPostExecute.
 */
public class NetworkResponse<T> {
    public static final int ERROR = 11941;
    public static final int ERROR_WRONG_SERVER = 11942;

    public String url;
    public int responseCode;
    public String json;
    public String headerLastModified = null;
    public String cookie = null;
    public T object;
    public Object object2;
    public String error;
    @StringRes
    public int errorMsg;
    public ServerError serverError;
    public boolean withoutErrorCheck = false;


    public NetworkResponse(String json, int responseCode) {
        this.json = json;
        this.responseCode = responseCode;
    }

    public NetworkResponse(int responseCode, T object) {
        this.object = object;
        this.responseCode = responseCode;
    }

    public NetworkResponse(String headerLastModified, String json, int responseCode, ServerError serverError) {
        this.headerLastModified = headerLastModified;
        this.json = json;
        this.responseCode = responseCode;
        this.serverError = serverError;
    }

    public NetworkResponse(String headerLastModified, String cookie, String json, int responseCode, ServerError
            serverError) {
        this.headerLastModified = headerLastModified;
        this.cookie = cookie;
        this.json = json;
        this.responseCode = responseCode;
        this.serverError = serverError;
    }


    public NetworkResponse(String error) {
        this.error = error;
        responseCode = ERROR;
    }

    public NetworkResponse(int errorMsg) {
        this.errorMsg = errorMsg;
        responseCode = ERROR;
    }

    public boolean isResponsePositive() {
        return responseCode < 300;
    }
}
