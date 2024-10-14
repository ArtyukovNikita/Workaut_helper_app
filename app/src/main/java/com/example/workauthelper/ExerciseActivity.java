package com.example.workauthelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;

    private ImageButton addButton; // Добавляем ImageButton для добавления
    private ImageButton searchButton; // Добавляем ImageButton для поиска

    private ArrayList<Exercise> exercises;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Инициализация элементов интерфейса упражнений
        listView = findViewById(R.id.exercise_list_view);
        TextView title = findViewById(R.id.activity_title);
        addButton = findViewById(R.id.add_button); // Инициализируем ImageButton
        searchButton = findViewById(R.id.search_button); // Инициализируем ImageButton

        // Установка названия активности
        title.setText("Упражнения");

        // Обработка нажатий на иконки
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddExerciseDialog dialog = new AddExerciseDialog(ExerciseActivity.this);
                dialog.show(); // Показать диалог
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Код для поиска активности
            }
        });


        // Инициализация списка упражнений
        exercises = new ArrayList<>();
        exercises.add(new Exercise("Отжимания", R.drawable.ic_push_up)); // Замените на вашу иконку
        exercises.add(new Exercise("Приседания", R.drawable.ic_squat)); // Замените на вашу иконку
        exercises.add(new Exercise("Бёрпи", R.drawable.ic_burpee)); // Замените на вашу иконку

        // Инициализация ListView
        listView = findViewById(R.id.exercise_list_view);

        // Создание адаптера и установка его на ListView
        ExerciseAdapter adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter);
    }
}
