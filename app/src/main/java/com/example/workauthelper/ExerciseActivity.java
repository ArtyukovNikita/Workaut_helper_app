package com.example.workauthelper;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;
    private String[] exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        listView = findViewById(R.id.exercise_list_view);
        String category = getIntent().getStringExtra("category");

        switch (category) {
            case "Спина":
                exercises = new String[]{"Становая тяга"};
                break;
            case "Ноги":
                exercises = new String[]{"Приседания"};
                break;
            case "Грудь":
                exercises = new String[]{"Жим лежа"};
                break;
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, exercises);
        listView.setAdapter(adapter);
    }
}
