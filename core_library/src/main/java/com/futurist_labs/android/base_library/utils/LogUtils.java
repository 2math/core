package com.futurist_labs.android.base_library.utils;

import android.os.Build;
import android.util.Log;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by Galeen on 22.7.2016 г..
 * Helper class to log data in console, also adds the log to Crashlytics.
 * Important, Crashlytics limits logs to 64kB. Crashlytics deletes older log entries
 * if a session's logs go over that limit.
 * Important, only "i"-information and "e"-error logs are printed to console and send
 * to Crashlitics in release build. In debug builds every thing is printed.
 */
public class LogUtils {
    private static final int MAX_TAG_LENGTH = 23;
    private static final String PREFIX = "AppLog ";
    private static Boolean isRunningTest = null;

    public static void d(String msg) {
        if (canLog()) {
            Log.d(PREFIX, prettyJson(msg));
        }
        logInCrashlitics(msg, PREFIX);
    }

    public static void d(String tag, String msg) {
        if (canLog()) {
            Log.d(makeTag(tag), prettyJson(msg));
        }
        logInCrashlitics(msg, tag);
    }

    public static void d(String tag, HashMap<String, String> postDataParams) {
        if (postDataParams != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> entry : postDataParams.entrySet()) {
                sb.append(entry.getKey());
                sb.append(" : ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            String msg = sb.toString();
            if (canLog()) Log.d(makeTag(tag), msg);
            logInCrashlitics(msg, tag);
        }
    }

    public static void w(String tag, String msg) {
        if (canLog()) {
            Log.w(makeTag(tag), prettyJson(msg));
        }
        logInCrashlitics(msg, tag);

    }

    /**
     * This method will a print in logcat only
     *
     * @param tag tag
     * @param msg message
     */
    public static void e(String tag, String msg) {
        LogUtils.e(tag, msg, null);
    }

    public static void e(String tag, String msg, Exception exception) {
        if (!isRunningTest()) {
            Log.e(makeTag(tag), prettyJson(msg, exception));
            if (exception != null && BaseLibraryConfiguration.getInstance().getCrashReporter() != null) {
                BaseLibraryConfiguration.getInstance().getCrashReporter().logException(exception);
            }
        } else {
            Log.e(makeTag(tag), msg != null ? msg : (exception != null ? exception.getLocalizedMessage() : "empty message"));
        }
    }

    /**
     * This method will print in logcat and Crashlitics
     *
     * @param tag tag
     * @param msg message
     */
    public static void error(String tag, String msg) {
        Exception exception = new Exception(tag + " :\n" + msg);
        LogUtils.e(tag, msg, exception);
    }


    public static void i(String tag, String msg) {
        if (!isRunningTest()) {
            Log.i(makeTag(tag), prettyJson(msg));
        }else{
            Log.i(makeTag(tag), msg);
        }
        logInCrashlitics(msg, tag);
    }

    /**
     * For network requests
     *
     * @param name
     * @param log
     */
    public static void net(String name, String log) {
        if (canLog())
            Log.w(makeTag(name), log);
    }

    public static String prettyJson(String body) {
        if (body == null || body.isEmpty()) {
            return "empty ot null";
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("\u00A0\u00A0");
            JsonElement jsonElement = new JsonParser().parse(body);
            gson.toJson(jsonElement, jsonWriter);
            String msg = stringWriter.toString();
            return msg;
        } catch (JsonParseException e) {
            return body;
        }
    }

    private static String prettyJson(String body, Exception exception) {
        if (body == null || body.isEmpty()) {
            if (exception != null) {
                body = exception.getLocalizedMessage();
                return prettyJson(body);
            }
        }
        return "body and exception are empty ot null";
    }

    private static void logInCrashlitics(String msg, String tag) {
        if (!isRunningTest() && BaseLibraryConfiguration.getInstance().getCrashReporter() != null) {
            BaseLibraryConfiguration.getInstance().getCrashReporter().log(tag, msg);
        }
    }

    private static boolean canLog() {
        return BaseLibraryConfiguration.getInstance().DEBUG && !isRunningTest();
    }

    private static String makeTag(String tag) {
        return makeTag(tag, true);
    }

    private static String makeTag(String tag, boolean withPrefix) {
        tag = PREFIX + tag;
        // Tag length limit was removed in API 24.
        if (tag.length() <= MAX_TAG_LENGTH || Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return tag;
        }
        return tag.substring(0, MAX_TAG_LENGTH);
    }

    private static boolean isRunningTest() {
        if (isRunningTest == null) {
            isRunningTest = SystemUtils.isRunningTest();
        }
        return isRunningTest;
    }

}
