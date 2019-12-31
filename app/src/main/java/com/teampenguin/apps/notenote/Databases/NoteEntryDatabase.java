package com.teampenguin.apps.notenote.Databases;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.teampenguin.apps.notenote.Daos.NoteEntryDao;
import com.teampenguin.apps.notenote.Models.NoteEntryM;

@Database(entities = {NoteEntryM.class}, version = 1)
public abstract class NoteEntryDatabase extends RoomDatabase {

    private static NoteEntryDatabase instance = null;
    public abstract NoteEntryDao noteEntryDao();

    public static NoteEntryDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteEntryDatabase.class, "note_entry_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }

        return instance;
    }
}
