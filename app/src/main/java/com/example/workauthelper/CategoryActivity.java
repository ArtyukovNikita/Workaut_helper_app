package com.example.workauthelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private ImageButton addButton;
    private ArrayAdapter<String> adapter;
    private List<String> categories; // Список категорий
    private List<Integer> categoryIds; // Список ID категорий

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = findViewById(R.id.category_list_view);
        TextView title = findViewById(R.id.activity_title);
        addButton = findViewById(R.id.add_button);

        title.setText("Категории");

        // Инициализация списков категорий и их ID
        categories = new ArrayList<>();
        categoryIds = new ArrayList<>();
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Получаем все категории из базы данных
        List<String> allCategories = dbHelper.getAllCategories();
        for (String category : allCategories) {
            categories.add(category);
            // Получаем ID категории и добавляем в список
            categoryIds.add(getCategoryIdByName(category, dbHelper));
        }

        // Установка адаптера для списка категорий
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ExerciseActivity.class);
                intent.putExtra("category_id", categoryIds.get(position)); // Передаем правильный ID категории
                intent.putExtra("category_name", categories.get(position));
                startActivity(intent);
            }
        });

        // Обработка нажатий на иконку добавления
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddCategoryDialog dialog = new AddCategoryDialog(CategoryActivity.this);
                dialog.setOnCategoryAddedListener(newCategory -> {
                    categories.add(newCategory); // Добавляем новую категорию в список
                    categoryIds.add(dbHelper.getCategoryIdByName(newCategory)); // Получаем ID новой категории
                    adapter.notifyDataSetChanged(); // Обновляем адаптер
                });
                dialog.show(); // Показать диалог
            }
        });
    }

    // Метод для получения ID категории по имени
    private int getCategoryIdByName(String categoryName, DatabaseHelper dbHelper) {
        // Здесь должен быть код для получения ID по имени категории
        // Например, запрос к базе данных
        return dbHelper.getCategoryIdByName(categoryName);
    }
}
