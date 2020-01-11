package com.teampenguin.apps.notenote.Models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Note_entry_photos")
public class NoteEntryPhoto {

    private static final String TAG = "NoteEntryPhoto";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "owner_note_id")
    private int ownerNoteId;

    @ColumnInfo(name = "base_64_encoded_string")
    private String base64EncodedString;

    @ColumnInfo(name = "created_date")
    private String createdDate;

    @ColumnInfo(name = "photo_caption")
    private String photoCaption;


    public NoteEntryPhoto()
    {
        ownerNoteId = -1;
        base64EncodedString = "";
        createdDate = "";
        photoCaption = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getOwnerNoteId() {
        return ownerNoteId;
    }

    public void setOwnerNoteId(int ownerNoteId) {
        this.ownerNoteId = ownerNoteId;
    }

    public String getBase64EncodedString() {
        return base64EncodedString;
    }

    public void setBase64EncodedString(String base64EncodedString) {
        this.base64EncodedString = base64EncodedString;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getPhotoCaption() {
        return photoCaption;
    }

    public void setPhotoCaption(String photoCaption) {
        this.photoCaption = photoCaption;
    }
}
