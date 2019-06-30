package com.app.pomodorotodo;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class TaskRepository {
    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository (Application application){
        TaskDatabase database = TaskDatabase.getInstance (application);
        taskDao = database.tasksDao ();
        allTasks = taskDao.getAllTasks ();
    }

    public void insert (Task task){
        new InsertAsyncTasks (taskDao).execute (task);
    }

    public void update (Task task){
        new UpdateAsyncTasks (taskDao).execute (task);
    }

    public void delete (Task task){
        new DeleteAsyncTasks (taskDao).execute (task);
    }

    public void deleteAllTasks (){
        new DeleteAllAsyncTasks (taskDao).execute ();
    }

    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }

    public static class InsertAsyncTasks extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private InsertAsyncTasks(TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground (Task... tasks){
            taskDao.insert (tasks[0]);
            return null;
        }
    }

    public static class UpdateAsyncTasks extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private UpdateAsyncTasks (TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground (Task... tasks){
            taskDao.update (tasks[0]);
            return null;
        }
    }

    public static class DeleteAsyncTasks extends AsyncTask<Task, Void, Void> {
        private TaskDao taskDao;

        private DeleteAsyncTasks (TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground (Task... tasks){
            taskDao.delete (tasks[0]);
            return null;
        }
    }

    public static class DeleteAllAsyncTasks extends AsyncTask<Void, Void, Void> {
        private TaskDao taskDao;

        private DeleteAllAsyncTasks (TaskDao taskDao){
            this.taskDao = taskDao;
        }

        @Override
        protected Void doInBackground (Void... voids){
            taskDao.deleteAllTasks();
            return null;
        }
    }
}