package com.teampenguin.apps.notenote.Databases;

import android.content.Context;
import android.content.Entity;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.DatabaseConfiguration;
import androidx.room.InvalidationTracker;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteOpenHelper;

import com.teampenguin.apps.notenote.Daos.TaskEntryDao;
import com.teampenguin.apps.notenote.Models.TaskEntryM;

@Database(entities = {TaskEntryM.class}, version = 1, exportSchema = false)
public abstract class TaskEntryDatabase extends RoomDatabase {

    private static TaskEntryDatabase instance = null;
    public abstract TaskEntryDao taskEntryDao();

    public static TaskEntryDatabase getInstance(Context context)
    {
        if(instance== null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TaskEntryDatabase.class, "task_entry_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
