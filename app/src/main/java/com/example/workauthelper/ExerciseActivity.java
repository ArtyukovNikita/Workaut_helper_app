package com.example.workauthelper;

import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ExerciseActivity extends AppCompatActivity {

    private ListView listView;
    private ImageButton addButton;
    private ArrayList<Exercise> exercises;
    private ExerciseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);

        // Инициализация компонентов UI
        listView = findViewById(R.id.exercise_list_view);
        addButton = findViewById(R.id.add_button);
        TextView title = findViewById(R.id.activity_title);

        // Получаем идентификатор категории из Intent
        int categoryId = getIntent().getIntExtra("category_id", -1);
        String categoryName = getIntent().getStringExtra("category_name");
        title.setText(categoryName); // Устанавливаем заголовок активности

        exercises = new ArrayList<>();
        loadExercisesFromDatabase(categoryId); // Загружаем упражнения из базы данных
        adapter = new ExerciseAdapter(this, exercises);
        listView.setAdapter(adapter); // Устанавливаем адаптер для списка

        // Обработка нажатия кнопки добавления упражнения
        addButton.setOnClickListener(v -> {
            AddExerciseDialog dialog = new AddExerciseDialog(ExerciseActivity.this);
            dialog.show();
        });

        // Обработка нажатия на элемент списка
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Exercise selectedExercise = exercises.get(position);
            // Получаем выбранный workout_id из SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("WorkoutHelper", MODE_PRIVATE);
            int workoutId = sharedPreferences.getInt("workout_id", -1); // Убедитесь, что вы сохраняете workout_id где-то
            // Сохраняем выбранное упражнение в базе данных
            addExerciseToWorkout(workoutId, selectedExercise.getId());
            Toast.makeText(ExerciseActivity.this, "Упражнение добавлено!", Toast.LENGTH_SHORT).show(); // Уведомление об успешном добавлении
        });
    }

    // Метод для загрузки упражнений из базы данных по категории
    private void loadExercisesFromDatabase(int categoryId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<Exercise> dbExercises = dbHelper.getExercisesByCategory(categoryId);
        exercises.clear();
        if (dbExercises != null) {
            exercises.addAll(dbExercises); // Добавляем загруженные упражнения в список
        }
    }

    // Метод для добавления упражнения в тренировку
    public void addExerciseToWorkout(int workoutId, int exerciseId) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        SQLiteDatabase database = dbHelper.getWritableDatabase(); // Получаем объект базы данных

        // Получаем сохраненную дату из SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("WorkoutHelper", MODE_PRIVATE);
        String selectedDate = sharedPreferences.getString("selected_date", null); // Получаем выбранную дату

        // Проверяем, существует ли тренировка с этой датой
        long newWorkoutId = dbHelper.getWorkoutIdByDate(selectedDate);

        // Если тренировка не существует, создаем новую
        if (newWorkoutId == -1) {
            newWorkoutId = dbHelper.addWorkout(selectedDate); // Добавляем новую тренировку
        }

        // Создание объекта ContentValues для вставки в базу данных
        ContentValues values = new ContentValues();
        values.put("workout_id", newWorkoutId); // Связываем с тренировкой
        values.put("exercise_id", exerciseId); // Связываем с упражнением

        // Вставка в базу данных
        database.insert("workout_exercises", null, values);

        // Закрытие базы данных
        database.close();
    }
}
