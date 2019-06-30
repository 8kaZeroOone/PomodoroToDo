package com.app.pomodorotodo;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity (tableName = "task_table") //создаём БД и задаём имя, а также описываем структуру бд
public class Task {

    @PrimaryKey (autoGenerate = true) //устанавливаем автогенерацию значений столбца id
    private int id;

    private String title;

    private String description;

    private int priority;

    public Task (String title, String description, int priority) { //создаём конструтор
        this.title = title;                                       //пропускаем id так как
        this.description = description;                           //он генерируется автоматически
        this.priority = priority;
    }

    public void setId (int id) { //метод Setter, с помощью которого в будущем
        this.id = id;           //будем передавать значения id строки БД
    }                           //для работы с ней

    public int getId () {
        return id;
    } //возвращает id задачи

    public String getTitle () {
        return title;
    } //возвращает имя задачи

    public String getDescription () {
        return description;
    } //возвращает описание задачи

    public int getPriority () {
        return priority;
    } //возвращает приоритет задачи
}
