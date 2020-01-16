package com.teampenguin.apps.notenote.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.teampenguin.apps.notenote.Models.TaskEntryM;
import com.teampenguin.apps.notenote.Repositories.TaskEntryRepository;

import java.util.List;

public class TaskEntryViewModel extends AndroidViewModel {

    public TaskEntryRepository repository;

    public TaskEntryViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskEntryRepository(application);
    }

    public LiveData<List<TaskEntryM>> getAllTaskEntries()
    {
        return repository.allTaskEntries;
    }

    public void insert(TaskEntryM taskEntry)
    {
        repository.insert(taskEntry);
    }

    public void update(TaskEntryM taskEntry)
    {
        repository.update(taskEntry);
    }

    public void delete(TaskEntryM taskEntry)
    {
        repository.delete(taskEntry);
    }
}
