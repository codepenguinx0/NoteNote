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

    public long insert(NoteEntryPhoto noteEntryPhoto)
    {
        InsertNoteEntryPhotoAsyncTask task = new InsertNoteEntryPhotoAsyncTask(dao);
        long id = -1;
        try {
            id = task.execute(noteEntryPhoto).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    public void update(NoteEntryPhoto noteEntryPhoto)
    {
        new UpdateNoteEntryPhotoAsyncTask(dao).execute(noteEntryPhoto);
    }

    public void delete(NoteEntryPhoto noteEntryPhoto)
    {
        new DeleteNoteEntryPhotoAsyncTask(dao).execute(noteEntryPhoto);
    }

    public List<NoteEntryPhoto> getNoteEntryPhotosByOwnerNoteId(int ownerNoteId, PhotoRepoResultsCallBack listener)
    {
        GetNoteEntryPhotosByOwnerNoteIdAsyncTask task = new GetNoteEntryPhotosByOwnerNoteIdAsyncTask(dao, listener);
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

    public List<NoteEntryPhoto> getAllPhotos()
    {
        GetAllPhotosAsyncTask task = new GetAllPhotosAsyncTask(dao);
        List<NoteEntryPhoto> list = new ArrayList<>();

        try {
            list = task.execute().get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return list;
    }

    private static class InsertNoteEntryPhotoAsyncTask extends AsyncTask<NoteEntryPhoto, Void, Long> {

        private NoteEntryPhotoDao dao;

        public InsertNoteEntryPhotoAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Long doInBackground(NoteEntryPhoto... noteEntryPhotos) {

            long id = dao.insert(noteEntryPhotos[0]);
            return id;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.d(TAG, "onPostExecute: finished inserting new id " + aLong);
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
        private PhotoRepoResultsCallBack listener;

        public GetNoteEntryPhotosByOwnerNoteIdAsyncTask(NoteEntryPhotoDao dao, PhotoRepoResultsCallBack listener)
        {
            this.dao = dao;
            this.listener = listener;
        }

        @Override
        protected List<NoteEntryPhoto> doInBackground(Integer... ints) {

            List<NoteEntryPhoto> list = dao.getNoteEntryPhotosByOwnerNoteId(ints[0]);
            return list;
        }

        @Override
        protected void onPostExecute(List<NoteEntryPhoto> noteEntryPhotos) {

            super.onPostExecute(noteEntryPhotos);

            if(listener!=null)
            {
                listener.returnListOfPhotos(noteEntryPhotos);
            }
        }
    }

    private static class GetAllPhotosAsyncTask extends AsyncTask<Void, Void, List<NoteEntryPhoto>>
    {
        private NoteEntryPhotoDao dao;

        public GetAllPhotosAsyncTask(NoteEntryPhotoDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected List<NoteEntryPhoto> doInBackground(Void... voids) {

            List<NoteEntryPhoto> list = dao.getAllPhotos();
            return list;
        }

        @Override
        protected void onPostExecute(List<NoteEntryPhoto> noteEntryPhotos) {
            super.onPostExecute(noteEntryPhotos);
            Log.d(TAG, "onPostExecute: noteEntryPhotos.size()" + noteEntryPhotos.size());
        }
    }

    public interface PhotoRepoResultsCallBack {
        void returnListOfPhotos(List<NoteEntryPhoto> photos);
    }

}
