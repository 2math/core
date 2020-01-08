package com.criapp_studio.coreapp;

import android.app.Application;

import com.criapp_studio.coreapp.model.unguarded.Login;
import com.criapp_studio.coreapp.repository.AppRepository;
import com.criapp_studio.coreapp.repository.MockRepository;
import com.criapp_studio.coreapp.repository.Repository;
import com.criapp_studio.coreapp.repository.json.AppJsonParser;
import com.criapp_studio.coreapp.repository.network.AppCalls;
import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;
import com.futurist_labs.android.base_library.repository.RepositoryFactory;
import com.futurist_labs.android.base_library.repository.network.Action;
import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.repository.network.ServerOperation;
import com.futurist_labs.android.base_library.repository.persistence.BasePersistenceManager;
import com.futurist_labs.android.base_library.repository.persistence.BaseSharedPreferenceManager;

import java.io.IOException;

import androidx.annotation.Nullable;

/**
 * Created by Galeen on 9/5/2018.
 */
public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        RepositoryFactory.init(new AppRepository(), new MockRepository());

        BaseLibraryConfiguration baseLibraryConfiguration = new BaseLibraryConfiguration(this, BuildConfig.DEBUG, BuildConfig.APPLICATION_ID, BuildConfig.BUILD_TYPE, BuildConfig.FLAVOR, BuildConfig.VERSION_CODE, BuildConfig.VERSION_NAME)
                .setServerUrl(BuildConfig.SERVER_URL)
                .setAddLocale(true)
                .setHeaderLocaleFieldName("Accept-Language")
                .setHeaderAuthorizationFieldName("x-auth-token")
                .setHeaderOS("x-os")
                .setHeaderOSValue("ANDROID_CLIENT")
                .setHeaderVersion("x-version")
                .setPersistenceManager(new BaseSharedPreferenceManager())
//                .setCrashReporter(new AppCrashReporter())
                .setFonts("fonts/Comfortaa-Regular.ttf",
                        "fonts/Comfortaa-Bold.ttf",
                        null,
                        "fonts/Comfortaa-Light.ttf")
                .setNetworkAuthoriser(new BaseLibraryConfiguration.NetworkAuthoriser() {
                    @Override
                    public NetworkResponse authorise() {
                        return authorize();
                    }
                });


        baseLibraryConfiguration.setHeaderLocaleFieldValue("en");

//        CrashlyticsCore crashlyticsCore = new CrashlyticsCore.Builder()
//                .disabled(BuildConfig.DEBUG)
//                .build();
//        Fabric.with(this, new Crashlytics.Builder().core(crashlyticsCore).build());

    }

    @Nullable
    public NetworkResponse authorize() {
        String[] creds = ((Repository) RepositoryFactory.getInstance().provide()).getCredentials();
        if (creds[0] != null) {
            try {
                Action action = AppCalls.getLoginAction(creds[0], creds[1]);
                NetworkResponse response = ServerOperation.doServerCall(action, null);
                if (response != null) {
                    Login login = AppJsonParser.readLogin(response.json);
                    if (login != null) {
                        BasePersistenceManager.saveToken(login.getSessionId());
                        response.object = login.getSessionId();
                    }
                }
                return response;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }
}
