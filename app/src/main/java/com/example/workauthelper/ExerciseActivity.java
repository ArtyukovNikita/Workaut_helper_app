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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        listView = findViewById(R.id.exercise_list_view);
        TextView title = findViewById(R.id.activity_title);
        addButton = findViewById(R.id.add_button);
        searchButton = findViewById(R.id.search_button);

        // Получаем ID категории из интента
        int categoryId = getIntent().getIntExtra("category_id", -1);
        String categoryName = getIntent().getStringExtra("category_name");
        title.setText(categoryName);

        exercises = new ArrayList<>();
        loadExercisesFromDatabase(categoryId); // Загружаем упражнения для конкретной категории
        adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter);

        addButton.setOnClickListener(v -> {
            AddExerciseDialog dialog = new AddExerciseDialog(ExerciseActivity.this);
            dialog.show();
        });

        searchButton.setOnClickListener(v -> {
            // Код для поиска упражнений
        });
        // Проверяем, открыта ли активность через btn_add
        boolean isAddingExercise = getIntent().getBooleanExtra("isAddingExercise", false);
        // Если открыта для добавления, активируем соответствующую логику
        if (isAddingExercise) {
            // Логика для добавления упражнения
        }
    }


    private void loadExercisesFromDatabase(int categoryId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Exercise> dbExercises = dbHelper.getExercisesByCategory(categoryId); // Получаем упражнения по категории
        exercises.clear();
        if (dbExercises != null) {
            exercises.addAll(dbExercises);
        }
    }

}
