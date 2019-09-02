package com.futurist_labs.android.base_library.utils.versions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Galeen on 8/27/2019.
 */
public class Versions implements Parcelable {
    private int currentVersion;
    private int minimalVersion;
    private String clientVersion;

    public int getCurrentVersion() {
        return currentVersion;
    }

    public void setCurrentVersion(int currentVersion) {
        this.currentVersion = currentVersion;
    }

    public int getMinimalVersion() {
        return minimalVersion;
    }

    public void setMinimalVersion(int minimalVersion) {
        this.minimalVersion = minimalVersion;
    }

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(final String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public Versions() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentVersion);
        dest.writeInt(this.minimalVersion);
        dest.writeString(this.clientVersion);
    }

    protected Versions(Parcel in) {
        this.currentVersion = in.readInt();
        this.minimalVersion = in.readInt();
        this.clientVersion = in.readString();
    }

    public static final Creator<Versions> CREATOR = new Creator<Versions>() {
        @Override
        public Versions createFromParcel(Parcel source) {
            return new Versions(source);
        }

        @Override
        public Versions[] newArray(int size) {
            return new Versions[size];
        }
    };
}
