package com.pws.pateast.api.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.pws.pateast.fragment.feeds.model.FeedList;

public class Feeds extends Response<FeedList> implements Parcelable {
    int more, id, userId, feedId, approved, controlUserId, likes, liked, feedType;
    String description, file, createdAt, reject_reason;
    Uri fileUri;
    int index;

    FeedList feedrecords;
    UserInfo user;

    public Feeds() {

    }

    protected Feeds(Parcel in) {
        more = in.readInt();
        id = in.readInt();
        userId = in.readInt();
        feedId = in.readInt();
        approved = in.readInt();
        controlUserId = in.readInt();
        likes = in.readInt();
        liked = in.readInt();
        feedType = in.readInt();
        description = in.readString();
        file = in.readString();
        createdAt = in.readString();
        reject_reason = in.readString();
        fileUri = in.readParcelable(Uri.class.getClassLoader());
        index = in.readInt();
        user = in.readParcelable(UserInfo.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(more);
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(feedId);
        dest.writeInt(approved);
        dest.writeInt(controlUserId);
        dest.writeInt(likes);
        dest.writeInt(liked);
        dest.writeInt(feedType);
        dest.writeString(description);
        dest.writeString(file);
        dest.writeString(createdAt);
        dest.writeString(reject_reason);
        dest.writeParcelable(fileUri, flags);
        dest.writeInt(index);
        dest.writeParcelable(user, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Feeds> CREATOR = new Creator<Feeds>() {
        @Override
        public Feeds createFromParcel(Parcel in) {
            return new Feeds(in);
        }

        @Override
        public Feeds[] newArray(int size) {
            return new Feeds[size];
        }
    };

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public int getApproved() {
        return approved;
    }

    public void setApproved(int approved) {
        this.approved = approved;
    }

    public int getControlUserId() {
        return controlUserId;
    }

    public void setControlUserId(int controlUserId) {
        this.controlUserId = controlUserId;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLiked() {
        return liked;
    }

    public void setLiked(int liked) {
        this.liked = liked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public FeedList getFeedrecords() {
        return feedrecords;
    }

    public void setFeedrecords(FeedList feedrecords) {
        this.feedrecords = feedrecords;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getReject_reason() {
        return reject_reason;
    }

    public void setReject_reason(String reject_reason) {
        this.reject_reason = reject_reason;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Feeds)) return false;

        Feeds media = (Feeds) o;

        if (index != media.index || file == null || media.file == null)
            return false;
        return file.equals(media.file);
    }

    @Override
    public int hashCode() {
        int result = index;
        result = 31 * result + file.hashCode();
        return result;
    }
}
