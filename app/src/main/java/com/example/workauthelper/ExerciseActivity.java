package com.example.workauthelper;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog; // Импорт AlertDialog
import android.view.LayoutInflater; // Импорт LayoutInflater
import android.view.View;
import android.widget.Button;
import android.widget.EditText; // Импорт EditText
import android.widget.ImageButton;
import android.widget.ImageView; // Импорт ImageView
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast; // Импорт Toast
import java.util.ArrayList;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;
    private ImageButton addButton; // Добавляем ImageButton для добавления
    private ImageButton searchButton; // Добавляем ImageButton для поиска
    private ArrayList<Exercise> exercises;
    private DatabaseHelper dbHelper; // Объявляем DatabaseHelper

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

        // Инициализация DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Обработка нажатий на иконки
        addButton.setOnClickListener(v -> showAddExerciseDialog());

        searchButton.setOnClickListener(v -> {
            // Код для поиска упражнения (не реализован)
        });

        // Инициализация списка упражнений
        exercises = dbHelper.getAllExercises(); // Получаем все упражнения из базы данных

        // Создание адаптера и установка его на ListView
        ExerciseAdapter adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter);
    }

    private void showAddExerciseDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Добавление упражнения");

        // Создание представления диалога
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_add_exercise, null);
        builder.setView(dialogView);

        EditText editTextName = dialogView.findViewById(R.id.edit_exercise_name);
        ImageView imageView = dialogView.findViewById(R.id.edit_exercise_image); // Изображение упражнения

        // Создание диалога
        AlertDialog dialog = builder.create();

        // Обработчик нажатия на кнопку "Добавить"
        dialogView.findViewById(R.id.add_button).setOnClickListener(v -> {
            String exerciseName = editTextName.getText().toString().trim();

            if (!exerciseName.isEmpty()) {
                // Вызов метода для добавления упражнения в базу данных
                addExerciseToDatabase(exerciseName);
                dialog.dismiss(); // Закрыть диалог после добавления
            } else {
                // Можно добавить сообщение об ошибке, если название пустое
                Toast.makeText(this, "Введите название упражнения", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия на кнопку "Отмена"
        dialogView.findViewById(R.id.cancel_button).setOnClickListener(v -> {
            dialog.dismiss(); // Закрыть диалог без добавления
        });

        dialog.show(); // Показываем диалог
    }

    private void addExerciseToDatabase(String exerciseName) {
        dbHelper.addExercise(exerciseName, "image_path", 1); // Укажите путь к изображению и ID категории
        // Обновите список упражнений после добавления
        updateExerciseList();
    }

    private void updateExerciseList() {
        exercises.clear(); // Очищаем текущий список
        exercises.addAll(dbHelper.getAllExercises()); // Получаем все упражнения из базы данных
        ((ExerciseAdapter) listView.getAdapter()).notifyDataSetChanged(); // Уведомляем адаптер об изменениях
    }
}