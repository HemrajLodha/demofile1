package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by intel on 25-Jan-18.
 */

public class StudyMaterial extends Response<Topic> implements Parcelable {
    protected StudyMaterial(Parcel in) {
    }

    public static final Creator<StudyMaterial> CREATOR = new Creator<StudyMaterial>() {
        @Override
        public StudyMaterial createFromParcel(Parcel in) {
            return new StudyMaterial(in);
        }

        @Override
        public StudyMaterial[] newArray(int size) {
            return new StudyMaterial[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
