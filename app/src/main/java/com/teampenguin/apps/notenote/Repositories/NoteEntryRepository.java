package com.teampenguin.apps.notenote.Repositories;


import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.teampenguin.apps.notenote.Daos.NoteEntryDao;
import com.teampenguin.apps.notenote.Models.NoteEntryM;
import com.teampenguin.apps.notenote.Databases.NoteEntryDatabase;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class NoteEntryRepository {

    private static final String TAG = "NoteEntryRepository";
    private NoteEntryDao noteEntryDao;
    private LiveData<List<NoteEntryM>> allNoteEntries;

    public NoteEntryRepository(Application application)
    {
        NoteEntryDatabase noteEntryDatabase = NoteEntryDatabase.getInstance(application);
        noteEntryDao =  noteEntryDatabase.noteEntryDao();
        allNoteEntries = noteEntryDao.getAllNoteEntries();
    }

    public long insert(NoteEntryM noteEntry)
    {
        Log.d(TAG, "insert: " + noteEntry.getCreateDate());

        long id = -1;

        InsertNoteEntryAsynTask task = new InsertNoteEntryAsynTask(noteEntryDao);

        try {
            id = task.execute(noteEntry).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return id;
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

    public void resetNotesCategory(String deletedCategory)
    {
        new ResetNotesCategoryAsyncTask(noteEntryDao).execute(deletedCategory);
    }

    public LiveData<List<NoteEntryM>> getAllNoteEntries()
    {
        return allNoteEntries;
    }

    public NoteEntryM getNoteEntryById(long id)
    {
        GetNoteEntryByIdAsyncTask task = new GetNoteEntryByIdAsyncTask(noteEntryDao);
        NoteEntryM noteEntry = null;

        try {
            noteEntry = task.execute(id).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return noteEntry;

    }

    private static class InsertNoteEntryAsynTask extends AsyncTask<NoteEntryM, Void, Long>
    {

        private NoteEntryDao noteEntryDao;

        public InsertNoteEntryAsynTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Long doInBackground(NoteEntryM... noteEntries) {

            long id = noteEntryDao.insert(noteEntries[0]);
            return id;
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

    private static class ResetNotesCategoryAsyncTask extends AsyncTask<String, Void, Void>
    {
        private NoteEntryDao noteEntryDao;

        public ResetNotesCategoryAsyncTask(NoteEntryDao noteEntryDao)
        {
            this.noteEntryDao = noteEntryDao;
        }

        @Override
        protected Void doInBackground(String... strings) {

            noteEntryDao.resetNotesCategory(strings[0]);
            return null;
        }
    }

    private static class GetNoteEntryByIdAsyncTask extends AsyncTask<Long, Void, NoteEntryM>
    {

        private NoteEntryDao dao;

        public GetNoteEntryByIdAsyncTask(NoteEntryDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected NoteEntryM doInBackground(Long... longs) {

            NoteEntryM noteEntry = dao.getNoteEntryById(longs[0]);
            return noteEntry;
        }
    }
}
