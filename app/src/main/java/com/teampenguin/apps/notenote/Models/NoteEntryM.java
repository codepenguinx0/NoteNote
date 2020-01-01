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

    @ColumnInfo(name = "author_name")
    private String authorName;

    @ColumnInfo(name = "author_id")
    private int authorId;

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

    @ColumnInfo(name = "is_active")
    private boolean isActive;


    public NoteEntryM()
    {
        authorName = "";
        authorId = 0;
        noteTitle = "No Title";
        createDate = Utils.convertDateToString(new Date());
        modifiedDate = "";
        content = "";
        category = 0;
        mood = 0;
        isActive = true;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
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

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
