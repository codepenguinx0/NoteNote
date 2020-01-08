package com.teampenguin.apps.notenote.Databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.teampenguin.apps.notenote.Daos.NoteEntryDao;
import com.teampenguin.apps.notenote.Models.NoteEntryM;

@Database(entities = {NoteEntryM.class}, version = 2)
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

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }
    };
}
