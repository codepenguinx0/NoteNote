package com.teampenguin.apps.notenote.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.teampenguin.apps.notenote.Models.NoteEntryM;

import java.util.List;

@Dao
public interface NoteEntryDao {

    @Insert
    void insert(NoteEntryM noteEntryM);

    @Delete
    void delete(NoteEntryM noteEntryM);

    @Update
    void update(NoteEntryM noteEntryM);

    @Query("SELECT * FROM note_entries ORDER BY id DESC")
    LiveData<List<NoteEntryM>> getAllNoteEntries();

    @Query("DELETE FROM note_entries")
    void deleteAllNoteEntries();

    @Query("UPDATE note_entries SET note_category = 'No Category' WHERE note_category = :deleteCategory")
    void resetNotesCategory(String deleteCategory);
}
