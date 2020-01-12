package com.teampenguin.apps.notenote.Repositories;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.teampenguin.apps.notenote.Daos.NoteEntryPhotoDao;
import com.teampenguin.apps.notenote.Databases.NoteEntryPhotoDatabase;
import com.teampenguin.apps.notenote.Models.NoteEntryPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class NoteEntryPhotoRepository {

    private static final String TAG = "NoteEntryPhotoRepositor";
    private NoteEntryPhotoDao dao;

    public NoteEntryPhotoRepository(Application application)
    {
        NoteEntryPhotoDatabase noteEntryPhotoDatabase = NoteEntryPhotoDatabase.getInstance(application);
        dao = noteEntryPhotoDatabase.noteEntryPhotoDao();
    }

    public void insert(NoteEntryPhoto noteEntryPhoto)
    {
        new InsertNoteEntryPhotoAsyncTask(dao).execute(noteEntryPhoto);
    }

    public void update(NoteEntryPhoto noteEntryPhoto)
    {
        new UpdateNoteEntryPhotoAsyncTask(dao).execute(noteEntryPhoto);
    }

    public void delete(NoteEntryPhoto noteEntryPhoto)
    {
        new DeleteNoteEntryPhotoAsyncTask(dao).execute(noteEntryPhoto);
    }

    public List<NoteEntryPhoto> getNoteEntryPhotosByOwnerNoteId(int ownerNoteId)
    {
        GetNoteEntryPhotosByOwnerNoteIdAsyncTask task = new GetNoteEntryPhotosByOwnerNoteIdAsyncTask(dao);
        List<NoteEntryPhoto> list;

        try {

            list = task.execute(ownerNoteId).get(60, TimeUnit.SECONDS);
            return list;

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }

    private static class InsertNoteEntryPhotoAsyncTask extends AsyncTask<NoteEntryPhoto, Void, Void> {

        private NoteEntryPhotoDao dao;

        public InsertNoteEntryPhotoAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteEntryPhoto... noteEntryPhotos) {

            dao.insert(noteEntryPhotos[0]);
            Log.d(TAG, "InsertNoteEntryPhotoAsyncTask: finished!");
            return null;
        }
    }

    private static class DeleteNoteEntryPhotoAsyncTask extends AsyncTask<NoteEntryPhoto, Void, Void> {

        private NoteEntryPhotoDao dao;

        public DeleteNoteEntryPhotoAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteEntryPhoto... noteEntryPhotos) {

            dao.delete(noteEntryPhotos[0]);
            return null;
        }
    }

    private static class UpdateNoteEntryPhotoAsyncTask extends AsyncTask<NoteEntryPhoto, Void, Void> {

        private NoteEntryPhotoDao dao;

        public UpdateNoteEntryPhotoAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(NoteEntryPhoto... noteEntryPhotos) {

            dao.update(noteEntryPhotos[0]);
            return null;
        }
    }

    private static class GetNoteEntryPhotosByOwnerNoteIdAsyncTask extends AsyncTask<Integer, Void, List<NoteEntryPhoto>>
    {
        private NoteEntryPhotoDao dao;

        public GetNoteEntryPhotosByOwnerNoteIdAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected List<NoteEntryPhoto> doInBackground(Integer... ints) {

            List<NoteEntryPhoto> list = dao.getNoteEntryPhotosByOwnerNoteId(ints[0]);
            return list;
        }

    }

}
