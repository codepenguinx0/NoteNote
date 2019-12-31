package com.teampenguin.apps.notenote.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.Date;

@Entity(tableName = "note_entries")
public class NoteEntryM {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "note_title")
    private String noteTitle;

    @ColumnInfo(name = "create_date")
    private String createDate;

    @ColumnInfo(name = "modified_date")
    private String modifiedDate;

    @ColumnInfo(name = "note_content")
    private String content;

    @ColumnInfo(name = "note_category")
    private int category;

    @ColumnInfo(name = "note_mood")
    private int mood;


    public NoteEntryM()
    {
        id = -1;
        noteTitle = "No Title";
        createDate = Utils.convertDateToString(new Date());
        modifiedDate = "";
        content = "";
        category = -1;
        mood = -1;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMood() {
        return mood;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
