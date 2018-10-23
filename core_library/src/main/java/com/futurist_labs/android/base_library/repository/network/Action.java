package com.futurist_labs.android.base_library.repository.network;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Galeen on 15.1.2016 г..
 * data holder with multiple constructors it has operation(PUT,GET,POST,DELETE or UPLOAD_FILE)
 * endpoint, params, headers,body and file which can be set to be send to the server from ServerOperation.
 */
public class Action {
    static final int HIGH_PRIORITY = 5;
    static final int NORMAL_PRIORITY = 10;//THREAD_PRIORITY_BACKGROUND
    static final int LOW_PRIORITY = 15;
    public static final int PUT = 1;
    public static final int GET = 2;
    public static final int POST = 3;
    public static final int DELETE = 4;
    public static final int UPLOAD_FILE = 5;
    public static final int DELAY_2_MIN = 6;
    public static final int GET_UNAUTHORIZED = 7;
    public static final int DOWNLOAD_FILE = 8;
    int operation;
    String endpoint;
    Map<String, String> params, headers;
    String body, toLog;
    File file;
    ArrayList<File> files;
    ArrayList<String> fileFields;
    boolean isFullUrl = false, isCheckServerUrl = true;
    int priority = NORMAL_PRIORITY;

    public Action() {
    }

    public Action(int operation, String endpoint, String body, Map<String, String> headers, Map<String, String> params) {
        this.body = body;
        this.endpoint = endpoint;
        this.headers = headers;
        this.operation = operation;
        this.params = params;
    }

    public Action(int operation, String endpoint,  Map<String, String> headers, Map<String, String> params) {
        this.endpoint = endpoint;
        this.headers = headers;
        this.operation = operation;
        this.params = params;
    }


    public Action(int operation, File file) {
        this.operation = operation;
        this.file = file;
    }

    public Action(int operation, String endpoint, String body, File file) {
        this.body = body;
        this.endpoint = endpoint;
        this.operation = operation;
        this.file = file;
    }

    public Action(int operation, String endpoint, Map<String, String> params, File file) {
        this.params = params;
        this.endpoint = endpoint;
        this.operation = operation;
        this.file = file;
    }

    public Action(int operation, String endpoint, String body) {
        this.body = body;
        this.endpoint = endpoint;
        this.operation = operation;
    }

    public Action(int operation, String endpoint, String body, String toLog) {
        this.body = body;
        this.endpoint = endpoint;
        this.operation = operation;
        this.toLog = toLog;
    }

    public Action(int operation, String endpoint, String body, boolean fullAddress) {
        this.body = body;
        this.endpoint = endpoint;
        this.operation = operation;
        isFullUrl = fullAddress;
    }

    public Action(String body) {
        this.body = body;
        this.endpoint = "";
        this.operation = POST;
    }

    public Action(String endpoint, Map<String, String> params, ArrayList<File> files, ArrayList<String> fileFields) {
        operation = UPLOAD_FILE;
        this.endpoint = endpoint;
        this.params = params;
        this.files = files;
        this.fileFields = fileFields;
    }

    public String getOperation() {
        String action = "send";
        switch (operation) {
            case Action.PUT:
                action = "PUT";
                break;
            case Action.POST:
                action = "POST";
                break;
            case Action.DELETE:
                action = "DELETE";
                break;
            case Action.UPLOAD_FILE:
                action = "UPLOAD_FILE";
                break;
            case Action.GET:
                action = "GET";
                break;
            case Action.DOWNLOAD_FILE:
                action = "DOWNLOAD_FILE";
                break;
        }
        return action;
    }

    public Action setOperation(int operation) {
        this.operation = operation;
        return this;
    }

    public Action setEndpoint(String endpoint) {
        this.endpoint = endpoint;
        return this;
    }

    public Action setParams(Map<String, String> params) {
        this.params = params;
        return this;
    }

    public Action setHeaders(Map<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public Action setBody(String body) {
        this.body = body;
        return this;
    }

    public Action setToLog(String toLog) {
        this.toLog = toLog;
        return this;
    }

    public Action setFile(File file) {
        this.file = file;
        return this;
    }

    public Action setFiles(ArrayList<File> files) {
        this.files = files;
        return this;
    }

    public Action setFileFields(ArrayList<String> fileFields) {
        this.fileFields = fileFields;
        return this;
    }

    public Action setFullUrl(boolean fullUrl) {
        isFullUrl = fullUrl;
        return this;
    }

    public Action setPriority(int priority) {
        this.priority = priority;
        return this;
    }

    public Action setIsChecjServerUrl(boolean checkServerUrl) {
        isCheckServerUrl = checkServerUrl;
        return this;
    }
}
