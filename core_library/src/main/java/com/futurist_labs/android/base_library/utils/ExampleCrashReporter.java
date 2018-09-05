package com.futurist_labs.android.base_library.utils;

import java.net.URI;

/**
 * Created by Galeen on 6/6/2018.
 * Simple example of crash reporter which reports to Crashlytics and TestFairy
 */
public class ExampleCrashReporter implements CrashReporter {
    @Override
    public void logException(Exception exception) {
//        if (exception != null) {
//            Crashlytics.logException(exception);
//            TestFairy.logThrowable(exception);
//        }
    }

    @Override
    public void log(String tag, String body) {
//        Crashlytics.log("tag - " + tag + " :\n" + body);
//        TestFairy.log(tag, body);
    }

    @Override
    public void setUserIdentifier(String identifier) {
//        if (identifier == null) identifier = "";
//        Crashlytics.setUserIdentifier(identifier);
//        TestFairy.setUserId(identifier);
    }

    @Override
    public void setValue(String key, String value) {
//        String formattedValue = LogUtils.prettyJson(value);
//        if (formattedValue == null) formattedValue = "";
//        Crashlytics.setString(key, formattedValue);
//        TestFairy.setAttribute(key, value);
    }

    @Override
    public void addEvent(String eventName) {
//        TestFairy.addEvent(eventName);
    }

    @Override
    public void setScreenName(String name) {
//        TestFairy.setScreenName(name);
    }

    @Override
    public void addNetworkEvent(URI uri, String method, int code, long startTimeMillis, long endTimeMillis, long requestSize, long responseSize, String errorMessage, String json) {
//        TestFairy.addNetworkEvent(uri, method, code, startTimeMillis, endTimeMillis, requestSize, responseSize,
//                "code : " + code +
//                        "\n error msg :" + errorMessage
//                        + "\n body" + json);
//        Crashlytics.log(method + " " + uri.toString() + "\ncode : " + code +
//                "\n error msg :" + errorMessage
//                + "\n body" + json);
    }
}
