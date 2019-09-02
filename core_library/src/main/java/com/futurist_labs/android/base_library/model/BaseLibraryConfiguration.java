package com.futurist_labs.android.base_library.model;

import android.app.Application;
import android.support.annotation.WorkerThread;

import com.futurist_labs.android.base_library.repository.network.NetworkResponse;
import com.futurist_labs.android.base_library.repository.persistence.BasePersistenceInterface;
import com.futurist_labs.android.base_library.utils.CrashReporter;
import com.futurist_labs.android.base_library.views.font_views.FontHelper;

/**
 * Created by Galeen on 5/18/2018.
 * this must be created in Application.onCreate ot off apps BuildConfig
 */
public class BaseLibraryConfiguration {
    private static BaseLibraryConfiguration instance;
    public  final boolean DEBUG ;
    public  final String APPLICATION_ID;
    public  final String BUILD_TYPE;
    public  final String FLAVOR;
    public  final int VERSION_CODE ;
    public  final String VERSION_NAME ;
    private CrashReporter crashReporter;
    private Application application;
    private String serverUrl, headerAuthorizationFieldName, headerLocaleFieldName, headerLocaleFieldValue, headerVersion, headerOS,
    headerOSValue;
    private boolean addLocale = false;
    private String regularFont = "fonts/Comfortaa-Regular.ttf";
    private BasePersistenceInterface persistenceManager;
    private NetworkAuthoriser networkAuthoriser;
    private String publicFileFolder;

    public static BaseLibraryConfiguration getInstance() {
        return instance;
    }

    public BaseLibraryConfiguration(Application application, boolean DEBUG, String APPLICATION_ID, String BUILD_TYPE, String FLAVOR, int VERSION_CODE, String VERSION_NAME) {
        instance = this;
        this.application = application;
        this.DEBUG = DEBUG;
        this.APPLICATION_ID = APPLICATION_ID;
        this.BUILD_TYPE = BUILD_TYPE;
        this.FLAVOR = FLAVOR;
        this.VERSION_CODE = VERSION_CODE;
        this.VERSION_NAME = VERSION_NAME;
    }
    public BaseLibraryConfiguration(Application application, boolean DEBUG, String APPLICATION_ID, String BUILD_TYPE, String FLAVOR, int VERSION_CODE, String VERSION_NAME, CrashReporter crashReporter) {
        this(application,DEBUG,APPLICATION_ID,  BUILD_TYPE,  FLAVOR,  VERSION_CODE,  VERSION_NAME);
        this.crashReporter = crashReporter;
    }

    public BaseLibraryConfiguration setCrashReporter(CrashReporter crashReporter) {
        this.crashReporter = crashReporter;
        return this;
    }

    public CrashReporter getCrashReporter() {
        return crashReporter;
    }

    public Application getApplication() {
        return application;
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public BaseLibraryConfiguration setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
        return this;
    }

    public boolean isAddLocale() {
        return addLocale;
    }

    public BaseLibraryConfiguration setAddLocale(boolean addLocale) {
        this.addLocale = addLocale;
        return this;
    }

    public String getHeaderAuthorizationFieldName() {
        return headerAuthorizationFieldName;
    }

    public BaseLibraryConfiguration setHeaderAuthorizationFieldName(String headerAuthorizationFieldName) {
        this.headerAuthorizationFieldName = headerAuthorizationFieldName;
        return this;
    }

    public String getHeaderLocaleFieldName() {
        return headerLocaleFieldName;
    }

    public BaseLibraryConfiguration setHeaderLocaleFieldName(String headerLocaleFieldName) {
        this.headerLocaleFieldName = headerLocaleFieldName;
        return this;
    }

    public String getHeaderLocaleFieldValue() {
        return headerLocaleFieldValue;
    }

    public BaseLibraryConfiguration setHeaderLocaleFieldValue(String headerLocaleFieldValue) {
        this.headerLocaleFieldValue = headerLocaleFieldValue;
        return this;
    }

    public String getHeaderVersion() {
        return headerVersion;
    }

    public BaseLibraryConfiguration setHeaderVersion(final String headerVersion) {
        this.headerVersion = headerVersion;
        return this;
    }

    public String getHeaderOS() {
        return headerOS;
    }

    public BaseLibraryConfiguration setHeaderOS(final String headerOS) {
        this.headerOS = headerOS;
        return this;
    }

    public String getHeaderOSValue() {
        return headerOSValue;
    }

    public BaseLibraryConfiguration setHeaderOSValue(final String headerOSValue) {
        this.headerOSValue = headerOSValue;
        return this;
    }

    public BasePersistenceInterface getPersistenceManager() {
        return persistenceManager;
    }

    public BaseLibraryConfiguration setPersistenceManager(BasePersistenceInterface persistenceManager) {
        this.persistenceManager = persistenceManager;
        return this;
    }

    public BaseLibraryConfiguration setFonts(String regular,String bold,String italic,String light) {
        regularFont = regular;
        FontHelper.init(bold, italic, light);
        return this;
    }

    public BaseLibraryConfiguration setAwesomeFont(String fontPath) {
        FontHelper.setAwesome(fontPath);
        return this;
    }

    public String getRegularFont() {
        return regularFont;
    }

    public NetworkAuthoriser getNetworkAuthoriser() {
        return networkAuthoriser;
    }

    public BaseLibraryConfiguration setNetworkAuthoriser(NetworkAuthoriser networkAuthoriser) {
        this.networkAuthoriser = networkAuthoriser;
        return this;
    }

    public String getPublicFileFolder() {
        return publicFileFolder;
    }

    public BaseLibraryConfiguration setPublicFileFolder(final String publicFileFolder) {
        this.publicFileFolder = publicFileFolder;
        return this;
    }

    public interface NetworkAuthoriser{
        /**
         * This method will be called in background if your call returns 401 (unauthorised)
         * Here you have to do a login to your server and return a NetworkResponse with token in NetworkResponse.object
         * @return NetworkResponse.object = session token , NetworkResponse.responseCode < 300 is positive
         */
        @WorkerThread
        NetworkResponse authorise();
    }
}
