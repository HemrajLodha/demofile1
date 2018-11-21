package com.pws.pateast.enums;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.IntDef;

import com.pws.pateast.api.model.Topic;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.pws.pateast.enums.LMSCategory.LMSType.NOTES;
import static com.pws.pateast.enums.LMSCategory.LMSType.PPT;
import static com.pws.pateast.enums.LMSCategory.LMSType.VIDEO;
import static com.pws.pateast.enums.LMSCategory.LMSType.WORK_SHEET;

/**
 * Created by intel on 24-Jan-18.
 */

public class LMSCategory implements Parcelable {


    @IntDef({VIDEO, PPT, NOTES, WORK_SHEET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LMSType {
        int VIDEO = 1;
        int PPT = 2;
        int NOTES = 3;
        int WORK_SHEET = 4;
    }

    private String title;
    @LMSType
    private int lmsType;
    private Topic topic;

    public LMSCategory(String title, @LMSType int lmsType, Topic topic) {
        setTitle(title);
        setLmsType(lmsType);
        setTopic(topic);
    }

    protected LMSCategory(Parcel in) {
        title = in.readString();
        lmsType = in.readInt();
        topic = in.readParcelable(Topic.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeInt(lmsType);
        dest.writeParcelable(topic, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LMSCategory> CREATOR = new Creator<LMSCategory>() {
        @Override
        public LMSCategory createFromParcel(Parcel in) {
            return new LMSCategory(in);
        }

        @Override
        public LMSCategory[] newArray(int size) {
            return new LMSCategory[size];
        }
    };

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @LMSType
    public int getLmsType() {
        return lmsType;
    }

    public void setLmsType(@LMSType int lmsType) {
        this.lmsType = lmsType;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public Topic getTopic() {
        return topic;
    }
}
