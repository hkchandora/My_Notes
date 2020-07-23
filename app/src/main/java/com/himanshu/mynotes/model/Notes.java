package com.himanshu.mynotes.model;

import androidx.annotation.NonNull;

public class Notes implements Comparable<Notes>{

    String nid, noteTitle, noteDesc, timeOfCreation, lastEditTime, tileColor, isPinned;

    public Notes() {
    }

    public Notes(String nid, String noteTitle, String noteDesc, String timeOfCreation, String lastEditTime, String tileColor, String isPinned) {
        this.nid = nid;
        this.noteTitle = noteTitle;
        this.noteDesc = noteDesc;
        this.timeOfCreation = timeOfCreation;
        this.lastEditTime = lastEditTime;
        this.tileColor = tileColor;
        this.isPinned = isPinned;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
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

    public String getIsPinned() {
        return isPinned;
    }

    public void setIsPinned(String isPinned) {
        this.isPinned = isPinned;
    }

    @Override
    public int compareTo(Notes notes) {
        return this.timeOfCreation.compareTo(notes.getTimeOfCreation());
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}