package com.criapp_studio.coreapp.repository.json;

import android.util.JsonReader;
import android.util.JsonWriter;

import com.criapp_studio.coreapp.BuildConfig;
import com.criapp_studio.coreapp.model.Info;
import com.criapp_studio.coreapp.model.unguarded.Login;
import com.criapp_studio.coreapp.model.unguarded.User;
import com.futurist_labs.android.base_library.repository.persistence.BaseJsonParser;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

/**
 * Created by Galeen on 9/5/2018.
 */
public class AppJsonParser extends BaseJsonParser {


    public static Login readLogin(String json){
        return fromJson(json, Login.class);
    }

    public static String writeLogin(String username, String password, boolean forLog) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name(JsonConstants.USERNAME).value(username);
        if (!forLog)
            writer.name(JsonConstants.PASSWORD).value(password);
//        writer.name(JsonConstants.LANGUAGE).value(PersistenceManager.getLanguage());
        addDeviceObject(writer);
        writer.endObject();
        writer.close();
        return sw.toString();
    }

    protected static void addDeviceObject(JsonWriter writer) throws IOException {
        writer.name("device");
        deviceData(writer);
    }

    protected static void deviceData(JsonWriter writer) throws IOException {
        writer.beginObject();
        writer.name(JsonConstants.INSTALLATION_ID).value("1111");
        writer.name("deviceLanguage").value("en");
//        }
        writer.name(JsonConstants.TYPE).value("ANDROID");
        writer.name("token").value("1234214214");//FirebaseInstanceId.getInstance().getToken());
        writer.name("useProductionForPushNotifications").value(BuildConfig.BUILD_TYPE.equalsIgnoreCase("production"));
        writer.endObject();
    }

    public static String writeUser(User user){
        return toJson(user, User.class);
    }

    public static User readUser(String json){
        return fromJson(json, User.class);
    }

    public static Info readInfo(String json) throws IOException {
        if (json == null) {
            return null;
        }
        String name = null, result = null;
        Info login = new Info();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.beginObject();
        while (reader.hasNext()) {
            name = reader.nextName();
            switch (name) {
                case JsonConstants.TITLE:
                    login.setTitle(BaseJsonParser.getString(reader));
                    break;
                case JsonConstants.DESCRIPTION:
                    login.setDescription(BaseJsonParser.getString(reader));
                    break;

                default:
                    reader.skipValue();
            }
        }
        reader.endObject();
        reader.close();
        return login;
    }

    public static String writeInfo(Info info) throws IOException {
        StringWriter sw = new StringWriter();
        JsonWriter writer = new JsonWriter(sw);
        writer.beginObject();
        writer.name(JsonConstants.TITLE).value(info.getTitle());
        writer.name(JsonConstants.DESCRIPTION).value(info.getDescription());
        writer.endObject();
        writer.close();
        return sw.toString();
    }
}
