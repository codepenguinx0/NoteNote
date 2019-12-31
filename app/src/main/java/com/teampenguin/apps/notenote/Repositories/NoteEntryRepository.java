package com.teampenguin.apps.notenote.Repositories;


import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.teampenguin.apps.notenote.Daos.NoteEntryDao;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.Databases.NoteEntryDatabase;

import java.util.List;

public class NoteEntryRepository {

    private NoteEntryDao noteEntryDao;
    private LiveData<List<NoteEntryM>> allNoteEntries;

    public NoteEntryRepository(Application application)
    {
        NoteEntryDatabase noteEntryDatabase = NoteEntryDatabase.getInstance(application);
        noteEntryDao =  noteEntryDatabase.noteEntryDao();
        allNoteEntries = noteEntryDao.getAllNoteEntries();
    }

    public void insert(NoteEntryM noteEntry)
    {
        new InsertNoteEntryAsynTask(noteEntryDao).execute(noteEntry);
    }

    public void delete(NoteEntryM noteEntry)
    {
        new DeleteNoteEntryAsynTask(noteEntryDao).execute(noteEntry);
    }

    public void update(NoteEntryM noteEntry)
    {
        new UpdateNoteEntryAsynTask(noteEntryDao).execute(noteEntry);
    }

    public void deleteAllNoteEntries()
    {
        new DeleteAllNoteEntriesAsynTask(noteEntryDao).execute();
    }

    public LiveData<List<NoteEntryM>> getAllNoteEntries()
    {
        return allNoteEntries;
    }

    private static class InsertNoteEntryAsynTask extends AsyncTask<NoteEntryM, Void, Void>
    {

        private NoteEntryDao noteEntryDao;

        public InsertNoteEntryAsynTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Void doInBackground(NoteEntryM... noteEntries) {

            noteEntryDao.insert(noteEntries[0]);
            return null;
        }
    }

    private static class DeleteNoteEntryAsynTask extends AsyncTask<NoteEntryM, Void, Void>
    {

        private NoteEntryDao noteEntryDao;

        public DeleteNoteEntryAsynTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Void doInBackground(NoteEntryM... noteEntries) {

            noteEntryDao.delete(noteEntries[0]);
            return null;
        }
    }

    private static class UpdateNoteEntryAsynTask extends AsyncTask<NoteEntryM, Void, Void>
    {

        private NoteEntryDao noteEntryDao;

        public UpdateNoteEntryAsynTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Void doInBackground(NoteEntryM... noteEntries) {

            noteEntryDao.update(noteEntries[0]);
            return null;
        }
    }

    private static class DeleteAllNoteEntriesAsynTask extends AsyncTask<Void, Void, Void>
    {

        private NoteEntryDao noteEntryDao;

        public DeleteAllNoteEntriesAsynTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {

            noteEntryDao.deleteAllNoteEntries();
            return null;
        }
    }
}
