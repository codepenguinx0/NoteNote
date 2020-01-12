package com.teampenguin.apps.notenote.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.Repositories.NoteEntryRepository;

import java.util.List;

public class NoteEntryViewModel extends AndroidViewModel {

    private NoteEntryRepository repository;
    private LiveData<List<NoteEntryM>> allNoteEntries;


    public NoteEntryViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteEntryRepository(application);
    }

    public long insert(NoteEntryM noteEntry)
    {
        return repository.insert(noteEntry);
    }

    public void update(NoteEntryM noteEntry)
    {
        repository.update(noteEntry);
    }

    public void delete(NoteEntryM noteEntry)
    {
        repository.delete(noteEntry);
    }

    public void deleteAll()
    {
        repository.deleteAllNoteEntries();
    }

    public LiveData<List<NoteEntryM>> getAllNoteEntries()
    {
        return repository.getAllNoteEntries();
    }

    public void resetNotesCategory(String category)
    {
        repository.resetNotesCategory(category);
    }

    public NoteEntryM getNoteEntryById(long id)
    {
        return repository.getNoteEntryById(id);
    }
}
