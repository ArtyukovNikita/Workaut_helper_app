package com.example.workauthelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Инициализация элементов интерфейса
        listView = findViewById(R.id.exercise_list_view);
        TextView title = findViewById(R.id.activity_title);
        Button addButton = findViewById(R.id.add_button);
        Button searchButton = findViewById(R.id.search_button);

        // Установка названия активности
        title.setText("Упражнения");

        // Обработка нажатий на кнопки
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Код для добавления нового упражнения
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Код для поиска упражнения
            }
        });
    }
}
