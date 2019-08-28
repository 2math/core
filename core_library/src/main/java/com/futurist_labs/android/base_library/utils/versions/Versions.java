package com.futurist_labs.android.base_library.utils.versions;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Galeen on 8/27/2019.
 */
public class Versions implements Parcelable {
    private int currentAndroidVersion;
    private int minimumAndroidVersion;

    public int getCurrentAndroidVersion() {
        return currentAndroidVersion;
    }

    public void setCurrentAndroidVersion(int currentAndroidVersion) {
        this.currentAndroidVersion = currentAndroidVersion;
    }

    public int getMinimumAndroidVersion() {
        return minimumAndroidVersion;
    }

    public void setMinimumAndroidVersion(int minimumAndroidVersion) {
        this.minimumAndroidVersion = minimumAndroidVersion;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.currentAndroidVersion);
        dest.writeInt(this.minimumAndroidVersion);
    }

    public Versions() {
    }

    protected Versions(Parcel in) {
        this.currentAndroidVersion = in.readInt();
        this.minimumAndroidVersion = in.readInt();
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
