package com.teampenguin.apps.notenote.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.teampenguin.apps.notenote.Models.TaskEntryM;

import java.util.List;

@Dao
public interface TaskEntryDao {

    @Insert
    void insert(TaskEntryM taskEntry);

    @Update
    void update(TaskEntryM taskEntry);

    @Delete
    void delete(TaskEntryM taskEntry);

    @Query("SELECT * FROM task_entries ORDER BY id DESC")
    LiveData<List<TaskEntryM>> getAllTaskEntries();


}
