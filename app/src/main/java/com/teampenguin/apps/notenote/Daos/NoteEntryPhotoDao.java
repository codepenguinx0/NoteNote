package com.teampenguin.apps.notenote.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;

import java.util.List;

@Dao
public interface NoteEntryPhotoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(NoteEntryPhoto noteEntryPhoto);

    @Delete
    void delete(NoteEntryPhoto noteEntryPhoto);

    @Update
    void update(NoteEntryPhoto noteEntryPhoto);

    @Query("SELECT * FROM note_entry_photos")
    List<NoteEntryPhoto> getAllPhotos();

    @Query("SELECT * FROM note_entry_photos WHERE owner_note_id = :ownerNoteId ORDER BY id")
    List<NoteEntryPhoto> getNoteEntryPhotosByOwnerNoteId(int ownerNoteId);
}
