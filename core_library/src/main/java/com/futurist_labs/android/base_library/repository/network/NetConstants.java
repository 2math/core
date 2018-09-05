package com.futurist_labs.android.base_library.repository.network;


import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

/**
 * Created by Galeen on 18.1.2016 г..
 * constants related to network requests
 */

public class NetConstants {
    public static final String SERVER_ADDRESS = BaseLibraryConfiguration.getInstance().getServerUrl();
    public static final String HEADER_LOCALE_FIELD = BaseLibraryConfiguration.getInstance().getHeaderLocaleFieldName();
    public static final boolean SHOULD_ADD_LOCALE = BaseLibraryConfiguration.getInstance().isAddLocale();
    public static final String HEADER_AUTHORIZATION = BaseLibraryConfiguration.getInstance().getHeaderAuthorizationFieldName();//"x-auth-token";
    public static final boolean DEBUG = BaseLibraryConfiguration.getInstance().DEBUG;
}