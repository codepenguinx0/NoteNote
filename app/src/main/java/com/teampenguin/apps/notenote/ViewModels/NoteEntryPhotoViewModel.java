package com.teampenguin.apps.notenote.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;
import com.teampenguin.apps.notenote.Repositories.NoteEntryPhotoRepository;

import java.util.List;

public class NoteEntryPhotoViewModel extends AndroidViewModel {

    private NoteEntryPhotoRepository repository;

    public NoteEntryPhotoViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteEntryPhotoRepository(application);
    }

    public long insert(NoteEntryPhoto noteEntryPhoto)
    {
        return repository.insert(noteEntryPhoto);
    }

    public void update(NoteEntryPhoto noteEntryPhoto)
    {
        repository.update(noteEntryPhoto);
    }

    public void delete(NoteEntryPhoto noteEntryPhoto)
    {
        repository.delete(noteEntryPhoto);
    }

    public List<NoteEntryPhoto> getNoteEntryPhotosByOwnerNoteId(int ownerNoteId, NoteEntryPhotoRepository.PhotoRepoResultsCallBack listener)
    {
        return repository.getNoteEntryPhotosByOwnerNoteId(ownerNoteId, listener);
    }

    public List<NoteEntryPhoto> getAllPhotos()
    {
        return repository.getAllPhotos();
    }

}
