package com.himanshu.mynotes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Notes implements Parcelable {

    private String noteId;
    private String noteTitle;
    private String noteDesc;
    private String timeOfCreation;
    private String lastEditTime;
    private String tileColor;
    private String deletedDate;
    private String deletedFrom;
    private boolean isPinned;
    private long createdTimeStamp;
    private long lastEditedTimeStamp;

    public Notes() {
    }

    protected Notes(Parcel in) {
        noteId = in.readString();
        noteTitle = in.readString();
        noteDesc = in.readString();
        timeOfCreation = in.readString();
        lastEditTime = in.readString();
        tileColor = in.readString();
        deletedDate = in.readString();
        deletedFrom = in.readString();
        isPinned = in.readByte() != 0;
        createdTimeStamp = in.readLong();
        lastEditedTimeStamp = in.readLong();
    }

    public static final Creator<Notes> CREATOR = new Creator<Notes>() {
        @Override
        public Notes createFromParcel(Parcel in) {
            return new Notes(in);
        }

        @Override
        public Notes[] newArray(int size) {
            return new Notes[size];
        }
    };

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getNoteDesc() {
        return noteDesc;
    }

    public void setNoteDesc(String noteDesc) {
        this.noteDesc = noteDesc;
    }

    public String getTimeOfCreation() {
        return timeOfCreation;
    }

    public void setTimeOfCreation(String timeOfCreation) {
        this.timeOfCreation = timeOfCreation;
    }

    public String getLastEditTime() {
        return lastEditTime;
    }

    public void setLastEditTime(String lastEditTime) {
        this.lastEditTime = lastEditTime;
    }

    public String getTileColor() {
        return tileColor;
    }

    public void setTileColor(String tileColor) {
        this.tileColor = tileColor;
    }

    public boolean getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(boolean isPinned) {
        this.isPinned = isPinned;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public String getDeletedFrom() {
        return deletedFrom;
    }

    public void setDeletedFrom(String deletedFrom) {
        this.deletedFrom = deletedFrom;
    }

    public long getLastEditedTimeStamp() {
        return lastEditedTimeStamp;
    }

    public void setLastEditedTimeStamp(long lastEditedTimeStamp) {
        this.lastEditedTimeStamp = lastEditedTimeStamp;
    }

    public String getDeletedDate() {
        return deletedDate;
    }

    public void setDeletedDate(String deletedDate) {
        this.deletedDate = deletedDate;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(noteId);
        parcel.writeString(noteTitle);
        parcel.writeString(noteDesc);
        parcel.writeString(timeOfCreation);
        parcel.writeString(lastEditTime);
        parcel.writeString(tileColor);
        parcel.writeString(deletedDate);
        parcel.writeString(deletedFrom);
        parcel.writeByte((byte) (isPinned ? 1 : 0));
        parcel.writeLong(createdTimeStamp);
        parcel.writeLong(lastEditedTimeStamp);
    }
}