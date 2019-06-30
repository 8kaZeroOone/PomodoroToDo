package com.app.pomodorotodo;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TaskDao { //интерфейс доступа к базе данных

    @Insert
    void insert (Task task); //добавляем в БД

    @Update
    void update (Task task); //обновляем БД

    @Delete
    void delete (Task task); //удаляем запись из БД

    @Query ("DELETE FROM task_table") //удаляем все записи из БД
    void deleteAllTasks ();

    @Query ("SELECT * FROM task_table ORDER BY priority ASC") //Сортировка БД по столбцу "приоритет"
    LiveData<List<Task>> getAllTasks ();
}
