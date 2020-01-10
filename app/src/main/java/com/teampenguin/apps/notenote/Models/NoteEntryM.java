package com.teampenguin.apps.notenote.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.teampenguin.apps.notenote.Utils.Utils;

import java.util.Date;

@Entity(tableName = "note_entries")
public class NoteEntryM implements Parcelable {

    public static final String DEFAULT_CATEGORY = "No Category";

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
    private String category;

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
        modifiedDate = Utils.convertDateToString(new Date());
        content = "";
        category = DEFAULT_CATEGORY;
        mood = 0;
        isActive = true;
    }

    protected NoteEntryM(Parcel in) {
        id = in.readInt();
        authorName = in.readString();
        authorId = in.readInt();
        noteTitle = in.readString();
        createDate = in.readString();
        modifiedDate = in.readString();
        content = in.readString();
        category = in.readString();
        mood = in.readInt();
        isActive = in.readByte() != 0;
    }

    public static final Creator<NoteEntryM> CREATOR = new Creator<NoteEntryM>() {
        @Override
        public NoteEntryM createFromParcel(Parcel in) {
            return new NoteEntryM(in);
        }

        @Override
        public NoteEntryM[] newArray(int size) {
            return new NoteEntryM[size];
        }
    };

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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(authorName);
        parcel.writeInt(authorId);
        parcel.writeString(noteTitle);
        parcel.writeString(createDate);
        parcel.writeString(modifiedDate);
        parcel.writeString(content);
        parcel.writeString(category);
        parcel.writeInt(mood);
        parcel.writeByte((byte) (isActive ? 1 : 0));
    }
}
