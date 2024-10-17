package com.example.workauthelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;
    private ImageButton addButton;
    private ImageButton searchButton;
    private ArrayList<Exercise> exercises;
    private ExerciseAdapter adapter;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        listView = findViewById(R.id.exercise_list_view);
        TextView title = findViewById(R.id.activity_title);
        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);
        title.setText("Упражнения");

        exercises = new ArrayList<>();
        loadExercisesFromDatabase();
        adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                AddExerciseDialog dialog = new AddExerciseDialog(ExerciseActivity.this);
                dialog.show();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                // Код для поиска активности
            }
        });
    }

    private void loadExercisesFromDatabase() {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Exercise> dbExercises = dbHelper.getAllExercises();
        if (dbExercises != null) {
            exercises.addAll(dbExercises);
        }
    }
}
