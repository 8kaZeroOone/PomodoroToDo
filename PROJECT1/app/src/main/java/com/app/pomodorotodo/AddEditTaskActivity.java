package com.app.pomodorotodo;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;

import maes.tech.intentanim.CustomIntent;

public class AddEditTaskActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String EXTRA_ID =
            "com.app.pomodorotodo.EXTRA_ID";
    public static final String EXTRA_TITLE =
            "com.app.pomodorotodo.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION =
            "com.app.pomodorotodo.EXTRA_DESCRIPTION";
    public static final String EXTRA_PRIORITY =
            "com.app.pomodorotodo.EXTRA_PRIORITY";

    private TextView priorityText, priorityCurrentValue;
    private TextInputLayout title,description;
    private FloatingActionButton priority1,priority2,priority3;
    public int currentPriority;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        priorityText = findViewById(R.id.text_priority);
        priorityCurrentValue = findViewById(R.id.current_priority);
        title = findViewById(R.id.input_name);
        description = findViewById(R.id.input_description);
        priority1 = findViewById(R.id.priority1);
        priority1.setOnClickListener(this);
        priority2 = findViewById(R.id.priority2);
        priority2.setOnClickListener(this);
        priority3 = findViewById(R.id.priority3);
        priority3.setOnClickListener(this);

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_24dp);

        Intent takeData = getIntent();

        if (takeData.hasExtra(EXTRA_ID)){
            setTitle("Редактировать задачу");
            priorityText.setText("Текущий приоритет задачи");
            title.getEditText().setText(takeData.getStringExtra((EXTRA_TITLE)));
            description.getEditText().setText(takeData.getStringExtra(EXTRA_DESCRIPTION));
            priorityCurrentValue.setText(toString().valueOf(takeData.getIntExtra(EXTRA_PRIORITY, 1)));

        }else{
            setTitle("Добавить задачу");
        }
    }

    private boolean validateName(){
        String nameInput =title.getEditText().getText().toString().trim();

        if(nameInput.isEmpty()){
            title.setError("Поле не может быть пустым");
            return false;
        } else if (nameInput.length()>50){
            title.setError("Слишком длинное название");
            return false;
        } else {
            title.setError(null);
            return true;
        }
    }

    private boolean validateDescription(){
        String descriptionInput =description.getEditText().getText().toString().trim();

        if(descriptionInput.isEmpty()){
            description.setError("Поле не может быть пустым");
            return false;
        } else if (descriptionInput.length()>270){
            description.setError("Слишком длинное название");
            return false;
        } else {
            description.setError(null);
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        if (!validateName()|!validateDescription()) {}
        else
            switch (v.getId()){
            case (R.id.priority1):
                currentPriority = 1;
                saveTask();
                break;
            case (R.id.priority2):
                currentPriority = 2;
                saveTask();
                break;
            case (R.id.priority3):
                currentPriority = 3;
                saveTask();
                break;
        }
    }


    private void saveTask(){
        String task_title = title.getEditText().getText().toString().trim();
        String task_description = description.getEditText().getText().toString().trim();

        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, task_title);
        data.putExtra(EXTRA_DESCRIPTION, task_description);
        data.putExtra(EXTRA_PRIORITY, currentPriority);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }

        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        CustomIntent.customType(this, "right-to-left");
    }
}