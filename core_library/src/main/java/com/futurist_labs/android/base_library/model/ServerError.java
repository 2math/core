package com.futurist_labs.android.base_library.model;

import java.util.ArrayList;

/**
 * Created by Galeen on 6/13/2018.
 */
public class ServerError {
    public static final String ACCESS_DENIED = "ACCESS_DENIED";
    public static final String INVALID_REQUEST_FORMAT = "INVALID_REQUEST_FORMAT";
    public static final String REQUEST_VALIDATION = "REQUEST_VALIDATION";
    public static final String INVALID_JSON = "INVALID_JSON";
    public static final String MISSING = "MISSING";
    public static final String CONSTRAINT_VIOLATION = "CONSTRAINT_VIOLATION";
    public static final String DUPLICATE_DATA = "DUPLICATE_DATA";
    public static final String UNKNOWN = "UNKNOWN";
    public static final String RESOURCE_NOT_FOUND = "RESOURCE_NOT_FOUND";
    public static final String USER_UPDATE_FAILED = "USER_UPDATE_FAILED";
    public static final String DUPLICATION_NOT_ALLOWED = "DUPLICATION_NOT_ALLOWED";
    public static final String TOO_YOUNG_FOR_REGISTRATION = "TOO_YOUNG_FOR_REGISTRATION";
    public static final String LOCATION_NOT_SUPPORTED = "LOCATION_NOT_SUPPORTED";
    public static final String PATIENT_YOUNGER_THAN_AN_ADULT = "PATIENT_YOUNGER_THAN_AN_ADULT";
    public static final String PATIENT_OLDER_THAN_A_CHILD = "PATIENT_OLDER_THAN_A_CHILD";
    public static final String ADULT_PATIENT_WITH_MONTHS = "ADULT_PATIENT_WITH_MONTHS";
    public static final String UNSUPPORTED_VERSION = "UNSUPPORTED_VERSION";

    private String type, code, text;
    private ArrayList<String> arguments;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public ArrayList<String> getArguments() {
        return arguments;
    }

    public void setArguments(ArrayList<String> arguments) {
        this.arguments = arguments;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
