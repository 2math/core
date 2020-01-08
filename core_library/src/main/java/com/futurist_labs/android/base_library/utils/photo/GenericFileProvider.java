package com.futurist_labs.android.base_library.utils.photo;

import android.database.Cursor;
import android.net.Uri;

import com.futurist_labs.android.base_library.model.BaseLibraryConfiguration;

import androidx.core.content.FileProvider;


/**
 * Created by Galeen on 12.7.2017 г..
 */

public class GenericFileProvider extends FileProvider {
    public static final String NAME = BaseLibraryConfiguration.getInstance().APPLICATION_ID+".fileprovider";
    public static final String FOLDER_NAME = "atHome";
    /**
     * //in Manifest
     *  <provider
     android:name="android.support.v4.content.FileProvider"
     android:authorities="${applicationId}.fileprovider"
     android:exported="false"
     android:grantUriPermissions="true">
     <meta-data
     android:name="android.support.FILE_PROVIDER_PATHS"
     android:resource="@xml/file_paths"/>
     </provider>

     //in res/xml folder
     <?xml version="1.0" encoding="utf-8"?>
     <paths xmlns:android="http://schemas.android.com/apk/res/android">
     <files-path name="files" path="/" />
     <external-path name="my_images" path="Android/data/com.futurist_labs.codehospitality/cache" />
     <external-path name="public_app_images" path="Pictures/CodeHospitality" />
     </paths>

     */

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return(new LegacyCompatCursorWrapper(super.query(uri, projection, selection, selectionArgs, sortOrder)));
    }
}
