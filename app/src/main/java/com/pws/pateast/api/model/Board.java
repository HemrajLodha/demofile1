package com.pws.pateast.api.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by intel on 24-Apr-17.
 */

public class Board implements Parcelable
{
    String createdAt,updatedAt;
    int id,masterId,is_active,stateId,countryId,languageId,boardId;
    String name,display_order,alias;

    ArrayList<Board> boarddetails;


    protected Board(Parcel in) {
        createdAt = in.readString();
        updatedAt = in.readString();
        id = in.readInt();
        masterId = in.readInt();
        is_active = in.readInt();
        stateId = in.readInt();
        countryId = in.readInt();
        languageId = in.readInt();
        boardId = in.readInt();
        name = in.readString();
        display_order = in.readString();
        alias = in.readString();
        boarddetails = in.createTypedArrayList(Board.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeInt(id);
        dest.writeInt(masterId);
        dest.writeInt(is_active);
        dest.writeInt(stateId);
        dest.writeInt(countryId);
        dest.writeInt(languageId);
        dest.writeInt(boardId);
        dest.writeString(name);
        dest.writeString(display_order);
        dest.writeString(alias);
        dest.writeTypedList(boarddetails);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Board> CREATOR = new Creator<Board>() {
        @Override
        public Board createFromParcel(Parcel in) {
            return new Board(in);
        }

        @Override
        public Board[] newArray(int size) {
            return new Board[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public int getMasterId() {
        return masterId;
    }

    public void setMasterId(int masterId) {
        this.masterId = masterId;
    }

    public int getIs_active() {
        return is_active;
    }

    public void setIs_active(int is_active) {
        this.is_active = is_active;
    }

    public int getStateId() {
        return stateId;
    }

    public void setStateId(int stateId) {
        this.stateId = stateId;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getLanguageId() {
        return languageId;
    }

    public void setLanguageId(int languageId) {
        this.languageId = languageId;
    }

    public int getBoardId() {
        return boardId;
    }

    public void setBoardId(int boardId) {
        this.boardId = boardId;
    }

    public String getDisplay_order() {
        return display_order;
    }

    public void setDisplay_order(String display_order) {
        this.display_order = display_order;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public ArrayList<Board> getBoarddetails() {
        return boarddetails;
    }

    public void setBoarddetails(ArrayList<Board> boarddetails) {
        this.boarddetails = boarddetails;
    }
}
