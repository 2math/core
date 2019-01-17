package com.futurist_labs.android.base_library.repository.network;

import com.futurist_labs.android.base_library.utils.LogUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.StringWriter;
import java.util.Date;
import java.util.Map;


/**
 * Created by Galeen on 28.1.2016 г..
 * helper class to log all send and income information in the logcat.
 */
public class GaleenTracker {
    private Date start;
    private String name;

    GaleenTracker(String name) {
        this.name = name;
    }

    void startTracker() {
        start = new Date(System.currentTimeMillis());
    }

    void stopTracker() {
        Date end = new Date(System.currentTimeMillis());
        LogUtils.w("GTr end " + name, "send:get:" + (end.getTime() - start.getTime()) / 1000.0);
    }

    public static void log(String name, String log) {
//        if (NetConstants.DEBUG)
        LogUtils.d(name, log);
    }

    public void logPrettyJson(String name, String json) {
//        if (NetConstants.DEBUG) {
        LogUtils.d(name.length() > 20 ? name.substring(0, 20) : name, prettyJson(json));
//        }
    }

    void logPrettyJson(Action action, String token) {
//        if (NetConstants.DEBUG) {
        String params = mapToString(action.params);
        String headers = mapToString(action.headers);
        String data = action.toLog == null ? action.body : action.toLog;
        LogUtils.d("GTr " + action.getOperation(), action.endpoint +
                (token == null ? "" : "\ntoken : " + token) +
                (action.body == null ? "" : "\n" + prettyJson(data)) +
                (params == null ? "" : "\nparams : \n" + params) +
                (headers == null ? "" : "\nheaders : \n" + headers));
//        }
    }

    private String mapToString(Map<String, String> map) {
        if (map != null) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
                sb.append("   ");
                sb.append(stringStringEntry.getKey());
                sb.append(" : ");
                sb.append(stringStringEntry.getValue());
                sb.append("\n");
            }
            return sb.toString();
        }
        return null;
    }

    void logError(Exception e, String log) {
        LogUtils.error("network error", name + "\n" + log);
    }

    void logResponse(NetworkResponse res) {
//        if (NetConstants.DEBUG) {
        if (res != null)
            LogUtils.d("GTr response", name + "\ncode : " +
                    res.responseCode + (res.headerLastModified != null ? "\nHeaderLastModified : " + res.headerLastModified : "")
                    + "\n" + prettyJson(res.json));
        else
            LogUtils.d("GTr response " + name, "null");
//        }
    }

    private String prettyJson(String body) {
        if (body == null || body.isEmpty()) {
            return "";
        }
        try {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            StringWriter stringWriter = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(stringWriter);
            jsonWriter.setIndent("\u00A0\u00A0");
            JsonElement jsonElement = new JsonParser().parse(body);
            gson.toJson(jsonElement, jsonWriter);
            return stringWriter.toString();
        } catch (JsonParseException e) {
            return body;
        }
    }
}
