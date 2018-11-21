package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 27-Jun-17.
 */

public class Tag extends Response<ArrayList<Tag>> implements Parcelable {
    int id, type;
    String description, title;

    ArrayList<Tag> tagdetails;
    ArrayList<Tag> tags;
    ArrayList<LeaveType> leaveTypes;
    Tag tag;

    public Tag(int id)
    {
        this.id = id;
    }

    public Tag(int id, String title) {
        this(id);
        tagdetails = new ArrayList<>();
        tagdetails.add(new Tag(title));
    }

    public Tag(String title) {
        this.title = title;
    }

    protected Tag(Parcel in) {
        id = in.readInt();
        type = in.readInt();
        description = in.readString();
        title = in.readString();
        tagdetails = in.createTypedArrayList(Tag.CREATOR);
        tags = in.createTypedArrayList(Tag.CREATOR);
        leaveTypes = in.createTypedArrayList(LeaveType.CREATOR);
        tag = in.readParcelable(Tag.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(type);
        dest.writeString(description);
        dest.writeString(title);
        dest.writeTypedList(tagdetails);
        dest.writeTypedList(tags);
        dest.writeTypedList(leaveTypes);
        dest.writeParcelable(tag, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Tag> CREATOR = new Creator<Tag>() {
        @Override
        public Tag createFromParcel(Parcel in) {
            return new Tag(in);
        }

        @Override
        public Tag[] newArray(int size) {
            return new Tag[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Tag> getTagdetails() {
        return tagdetails;
    }

    public void setTagdetails(ArrayList<Tag> tagdetails) {
        this.tagdetails = tagdetails;
    }

    public ArrayList<LeaveType> getLeaveTypes() {
        return leaveTypes;
    }

    public void setLeaveTypes(ArrayList<LeaveType> leaveTypes) {
        this.leaveTypes = leaveTypes;
    }

    public ArrayList<Tag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<Tag> tags) {
        this.tags = tags;
    }

    public Tag getTag() {
        return tag;
    }

    public static Tag getTag(int id, String title) {
        Tag tag = new Tag(id, title);

        return tag;
    }


    @Override
    public boolean equals(Object obj) {
        return getId() == ((Tag) obj).getId();
    }
}
