package com.example.workauthelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;

    private ImageButton addButton; // Добавляем ImageButton для добавления
    private ImageButton searchButton; // Добавляем ImageButton для поиска

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Инициализация элементов интерфейса
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
                // Код для добавления новой категории
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Код для поиска категории
            }
        });
    }
}
