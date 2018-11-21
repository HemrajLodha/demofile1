package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.utils.DateUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by intel on 22-Aug-17.
 */

public class Holiday extends Response<ArrayList<Holiday>> implements Parcelable {
    int id,holidayId,masterId,languageId,is_active;
    String name,start_date,end_date;

    ArrayList<Holiday> holidaydetails;

    protected Holiday(Parcel in) {
        id = in.readInt();
        holidayId = in.readInt();
        masterId = in.readInt();
        languageId = in.readInt();
        is_active = in.readInt();
        name = in.readString();
        start_date = in.readString();
        end_date = in.readString();
        holidaydetails = in.createTypedArrayList(Holiday.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(holidayId);
        dest.writeInt(masterId);
        dest.writeInt(languageId);
        dest.writeInt(is_active);
        dest.writeString(name);
        dest.writeString(start_date);
        dest.writeString(end_date);
        dest.writeTypedList(holidaydetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Holiday> CREATOR = new Creator<Holiday>() {
        @Override
        public Holiday createFromParcel(Parcel in) {
            return new Holiday(in);
        }

        @Override
        public Holiday[] newArray(int size) {
            return new Holiday[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getHolidayId() {
        return holidayId;
    }

    public void setHolidayId(int holidayId) {
        this.holidayId = holidayId;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public ArrayList<Holiday> getHolidaydetails() {
        return holidaydetails;
    }

    public void setHolidaydetails(ArrayList<Holiday> holidaydetails) {
        this.holidaydetails = holidaydetails;
    }

    public String getMonth(Calendar calendar)
    {
        calendar.setTime(DateUtils.parse(getStart_date(), DateUtils.DATE_FORMAT_PATTERN));
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.ENGLISH);
    }
}
