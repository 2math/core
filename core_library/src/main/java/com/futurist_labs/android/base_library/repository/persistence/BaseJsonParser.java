package com.futurist_labs.android.base_library.repository.persistence;

import android.util.JsonReader;
import android.util.JsonToken;

import com.futurist_labs.android.base_library.model.ServerError;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Galeen on 21.10.2016 г..
 * read/write json to model objects
 */
public class BaseJsonParser {

    public static <T> T fromJson(String json, Class<T> classOfT) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, (Type) classOfT);
    }

    public static <T> String toJson(T object, Class<T> classOfT) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.toJson(object, classOfT);
    }

    public static <T> T fromJson(String json, Type type) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.fromJson(json, type);
    }

    public static <T> String toJson(T object, Type type) throws JsonSyntaxException {
        Gson gson = new Gson();
        return gson.toJson(object, type);
    }

    public static String getString(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.NULL)
            return reader.nextString();
        else
            reader.skipValue();
        return null;
    }

    public static Date getDate(JsonReader reader, SimpleDateFormat dateFormat) throws IOException {
        if (reader.peek() != JsonToken.NULL)
            try {
                return dateFormat.parse(reader.nextString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        else
            reader.skipValue();
        return null;
    }

    public static Integer getInt(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.NULL)
            return reader.nextInt();
        else
            reader.skipValue();
        return -1;
    }

    public static boolean getBoolean(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.NULL)
            return reader.nextBoolean();
        else
            reader.skipValue();
        return false;
    }

    public static double getDouble(JsonReader reader) throws IOException {
        if (reader.peek() != JsonToken.NULL)
            return reader.nextDouble();
        else
            reader.skipValue();
        return -1;
    }

    public static ServerError readError(String error) {
        if (error == null || error.length() == 0) {
            return null;
        }
        String name;
        ServerError serverError = new ServerError();
        JsonReader reader = new JsonReader(new StringReader(error));
        try {
            reader.beginObject();
            while (reader.hasNext()) {
                name = reader.nextName();
                switch (name) {
                    case "code":
                        serverError.setCode(getString(reader));
                        break;
                    case "type":
                        serverError.setType(getString(reader));
                        break;
                    case "arguments":
                        if (reader.peek() != JsonToken.NULL) {
                            reader.beginArray();
                            ArrayList<String> arguments = new ArrayList<>();
                            while (reader.hasNext()) {
                                arguments.add(getString(reader));
                            }
                            serverError.setArguments(arguments);
                            reader.endArray();
                        }
                        break;
                    default:
                        reader.skipValue();
                }
            }
            reader.endObject();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            serverError.setText(error);
        }
        return serverError;
    }

    public static String readFileId(NetworkResponse response, String fieldName) throws IOException {
        if (response.json == null) {
            return null;
        }
        String name = null, result = null;
        String id = null;
        JsonReader reader = new JsonReader(new StringReader(response.json));
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            if (name.equals(fieldName)) {
                id = BaseJsonParser.getString(reader);
            } else {
                reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
        return id;
    }
}
