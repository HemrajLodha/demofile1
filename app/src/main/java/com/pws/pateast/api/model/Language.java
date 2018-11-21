package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by intel on 21-Apr-17.
 */

public class Language implements Parcelable
{
    int id,is_active;
    String name,code,direction;

    protected Language(Parcel in) {
        id = in.readInt();
        is_active = in.readInt();
        name = in.readString();
        code = in.readString();
        direction = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(is_active);
        dest.writeString(name);
        dest.writeString(code);
        dest.writeString(direction);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Language> CREATOR = new Creator<Language>() {
        @Override
        public Language createFromParcel(Parcel in) {
            return new Language(in);
        }

        @Override
        public Language[] newArray(int size) {
            return new Language[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public static String[] getNameArray(List<Language> languages) {
        if (languages == null)
            languages = new ArrayList<>();
        String[] languageArray = new String[languages.size()];
        for (Language item : languages) {
            languageArray[languages.indexOf(item)] = item.getName();
        }
        return languageArray;
    }

    public static int getSelectedPosition(List<Language> languages, String code) {
        if (languages == null)
            languages = new ArrayList<>();
        int pos = -1;
        for (Language item : languages)
        {
            if (item.getCode().equalsIgnoreCase(code)) {
                pos = languages.indexOf(item);
                break;
            }
        }
        return pos;
    }
}
