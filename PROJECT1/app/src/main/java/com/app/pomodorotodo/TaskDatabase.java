package com.app.pomodorotodo;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class TaskDatabase extends RoomDatabase {

    private static TaskDatabase instance;

    public abstract TaskDao tasksDao ();

    public static synchronized TaskDatabase getInstance (Context context){
        if (instance==null){
            instance = Room.databaseBuilder(context.getApplicationContext (),
                    TaskDatabase.class, "tasks_database")
                    .fallbackToDestructiveMigration ()
                    .addCallback (roomCallback)
                    .build ();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback (){
        @Override
        public void onCreate (@NonNull SupportSQLiteDatabase db) {
            super.onCreate (db);
        }
    };
}
