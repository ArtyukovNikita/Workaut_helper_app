package com.example.workauthelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton; // Импортируем ImageButton
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private String[] categories = {"Спина", "Ноги", "Грудь"};
    private ImageButton addButton; // Добавляем ImageButton для добавления
    private ImageButton searchButton; // Добавляем ImageButton для поиска

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        // Инициализация элементов интерфейса
        listView = findViewById(R.id.category_list_view);
        TextView title = findViewById(R.id.activity_title);
        addButton = findViewById(R.id.add_button); // Инициализируем ImageButton
        searchButton = findViewById(R.id.search_button); // Инициализируем ImageButton

        // Установка названия активности
        title.setText("Категории");

        // Установка адаптера для списка категорий
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ExerciseActivity.class);
                intent.putExtra("category", categories[position]);
                startActivity(intent);
            }
        });

        // Обработка нажатий на иконки
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Вы можете создать аналогичный диалог для добавления категории
                AddExerciseDialog dialog = new AddExerciseDialog(CategoryActivity.this);
                dialog.show(); // Показать диалог
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
