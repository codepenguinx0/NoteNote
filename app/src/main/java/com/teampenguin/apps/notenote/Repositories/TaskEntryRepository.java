package com.teampenguin.apps.notenote.Repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.teampenguin.apps.notenote.Daos.TaskEntryDao;
import com.teampenguin.apps.notenote.Databases.TaskEntryDatabase;
import com.teampenguin.apps.notenote.Models.TaskEntryM;

import java.util.List;

public class TaskEntryRepository {

    private TaskEntryDao dao;
    public LiveData<List<TaskEntryM>> allTaskEntries;

    public TaskEntryRepository(Application application)
    {
        TaskEntryDatabase database = TaskEntryDatabase.getInstance(application);
        dao = database.taskEntryDao();
        allTaskEntries = dao.getAllTaskEntries();
    }

    public void insert(TaskEntryM taskEntryM)
    {
        new InsertTaskEntryAsyncTask(dao).execute(taskEntryM);
    }

    public void update(TaskEntryM taskEntryM)
    {
        new UpdateTaskEntryAsyncTask(dao).execute(taskEntryM);
    }

    public void delete(TaskEntryM taskEntryM)
    {
        new DeleteTaskEntryAsyncTask(dao).execute(taskEntryM);
    }

    private static class InsertTaskEntryAsyncTask extends AsyncTask<TaskEntryM,Void,Void>
    {
        private TaskEntryDao dao;

        public InsertTaskEntryAsyncTask(TaskEntryDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(TaskEntryM... taskEntries) {

            dao.insert(taskEntries[0]);
            return null;
        }
    }

    private static class UpdateTaskEntryAsyncTask extends AsyncTask<TaskEntryM,Void,Void>
    {
        private TaskEntryDao dao;

        public UpdateTaskEntryAsyncTask(TaskEntryDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(TaskEntryM... taskEntries) {

            dao.update(taskEntries[0]);
            return null;
        }
    }

    private static class DeleteTaskEntryAsyncTask extends AsyncTask<TaskEntryM,Void,Void>
    {
        private TaskEntryDao dao;

        public DeleteTaskEntryAsyncTask(TaskEntryDao dao)
        {
            this.dao = dao;
        }

        @Override
        protected Void doInBackground(TaskEntryM... taskEntries) {

            dao.delete(taskEntries[0]);
            return null;
        }
    }
}
