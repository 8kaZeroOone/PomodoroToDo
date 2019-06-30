package com.app.pomodorotodo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.List;

import maes.tech.intentanim.CustomIntent;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public static final int ADD_TASK_REQUEST = 1;
    public static final int EDIT_TASK_REQUEST = 2;
    public static final int TASK_TO_TIMER_REQUEST = 3;

    private TaskViewModel taskViewModel;
    private FloatingActionButton add, timer, about;
    private FloatingActionsMenu fam;
    private ImageView whenEmpty_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("PomodoroToDo");
        getSupportActionBar().setSubtitle("Список дел");

        whenEmpty_img = findViewById(R.id.empty_image);
        fam = findViewById(R.id.fab_expand_menu_button);
        add = findViewById(R.id.fab_add);
        add.setOnClickListener(this);
        timer = findViewById(R.id.fab_timer);
        timer.setOnClickListener(this);
        about = findViewById(R.id.fab_about);
        about.setOnClickListener(this);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final TaskAdapter adapter = new TaskAdapter();
        recyclerView.setAdapter(adapter);

        taskViewModel = ViewModelProviders.of(this).get(TaskViewModel.class);
        taskViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(@Nullable List<Task> tasks) {
                //обновление (перерисовка) RecyclerView
                adapter.submitList(tasks);
                if (tasks.isEmpty()) {
                    whenEmpty_img.setVisibility(View.VISIBLE);
                } else {
                    whenEmpty_img.setVisibility(View.INVISIBLE);
                }
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                taskViewModel.delete(adapter.getTaskAt(viewHolder.getAdapterPosition()));
                Toast.makeText(MainActivity.this, "Задача удалена", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);


        adapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Task task) {
                Intent timerTask = new Intent(MainActivity.this, TimerActivity.class);
                timerTask.putExtra(TimerActivity.EXTRA_ID, task.getId());
                timerTask.putExtra(TimerActivity.EXTRA_TITLE, task.getTitle());

                startActivityForResult(timerTask, TASK_TO_TIMER_REQUEST);
                CustomIntent.customType(MainActivity.this, "left-to-right");
            }
        });

        adapter.setOnItemLongClickListener(new TaskAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(Task task) {
                Intent editTask = new Intent(MainActivity.this, AddEditTaskActivity.class);
                editTask.putExtra(AddEditTaskActivity.EXTRA_ID, task.getId());
                editTask.putExtra(AddEditTaskActivity.EXTRA_TITLE, task.getTitle());
                editTask.putExtra(AddEditTaskActivity.EXTRA_DESCRIPTION, task.getDescription());
                editTask.putExtra(AddEditTaskActivity.EXTRA_PRIORITY, task.getPriority());

                startActivityForResult(editTask, EDIT_TASK_REQUEST);
                CustomIntent.customType(MainActivity.this, "left-to-right");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_all:
                taskViewModel.deleteAllTasks();
                Toast.makeText(MainActivity.this, "Все задачи удалены", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case (R.id.fab_add):
                Intent addContent = new Intent(MainActivity.this, AddEditTaskActivity.class);
                startActivityForResult(addContent, ADD_TASK_REQUEST);
                CustomIntent.customType(this, "left-to-right");
                fam.collapse();
                break;

            case (R.id.fab_timer):
                Intent timer = new Intent(MainActivity.this, TimerActivity.class);
                startActivity(timer);
                CustomIntent.customType(this, "left-to-right");
                fam.collapse();
                break;

            case (R.id.fab_about):
                Intent about = new Intent(MainActivity.this, AboutActivity.class);
                startActivity(about);
                CustomIntent.customType(this, "fadein-to-fadeout");
                fam.collapse();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK) {
            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

            Task task = new Task(title, description, priority);
            taskViewModel.insert(task);

            Toast.makeText(this, "Задача сохранена", Toast.LENGTH_SHORT).show();
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddEditTaskActivity.EXTRA_ID, -1);

            if (id == -1) {
                Toast.makeText(this, "Задача не может быть сохранена", Toast.LENGTH_SHORT).show();
                return;
            }

            String title = data.getStringExtra(AddEditTaskActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddEditTaskActivity.EXTRA_DESCRIPTION);
            int priority = data.getIntExtra(AddEditTaskActivity.EXTRA_PRIORITY, 1);

            Task task = new Task(title, description, priority);
            task.setId(id);
            taskViewModel.update(task);

            Toast.makeText(this, "Задача успешно изменена", Toast.LENGTH_SHORT).show();
        } else if (requestCode == TASK_TO_TIMER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(TimerActivity.EXTRA_ID, -1);
            Task task = new Task(null, null, 1);
            task.setId(id);
            taskViewModel.delete(task);
        } else {
            Toast.makeText(this, "Отмена изменений", Toast.LENGTH_SHORT).show();
        }
    }
}