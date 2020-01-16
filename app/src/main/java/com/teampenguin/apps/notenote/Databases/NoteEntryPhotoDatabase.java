package com.teampenguin.apps.notenote.Databases;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.teampenguin.apps.notenote.Daos.NoteEntryPhotoDao;
import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;

@Database(entities = {NoteEntryPhoto.class}, version = 2, exportSchema = false)
public abstract class NoteEntryPhotoDatabase extends RoomDatabase {

    private static NoteEntryPhotoDatabase instance = null;
    public abstract NoteEntryPhotoDao noteEntryPhotoDao();

    public static NoteEntryPhotoDatabase getInstance(Context context)
    {
        if(instance==null)
        {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NoteEntryPhotoDatabase.class, "note_entry_photos_database")
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
