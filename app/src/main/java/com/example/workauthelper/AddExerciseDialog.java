package com.example.workauthelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddExerciseDialog {

    private Dialog dialog;
    private EditText exerciseNameEditText;
    private ImageView exerciseImageView;
    private Spinner categorySpinner;

    public AddExerciseDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_exercise);
        exerciseNameEditText = dialog.findViewById(R.id.edit_exercise_name);
        exerciseImageView = dialog.findViewById(R.id.edit_exercise_image);
        categorySpinner = dialog.findViewById(R.id.category_spinner);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<String> categories = dbHelper.getAllCategories(); // Получаем категории из базы данных

        // Установка адаптера для Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button addButton = dialog.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dialog.dismiss()); // Закрыть диалог

        addButton.setOnClickListener(v -> {
            String exerciseName = exerciseNameEditText.getText().toString();
            String selectedCategory = categorySpinner.getSelectedItem().toString();

            // Проверка, что имя упражнения не пустое
            if (!exerciseName.isEmpty()) {

                dbHelper.addExercise(exerciseName, selectedCategory); // Добавляем упражнение в базу данных
                dialog.dismiss(); // Закрыть диалог
            } else {
                // Вы можете добавить уведомление для пользователя, что имя упражнения не может быть пустым
                Toast.makeText(context, "Введите название упражнения", Toast.LENGTH_SHORT).show();
            }
        });

        // Обработчик нажатия на изображение
        exerciseImageView.setOnClickListener(v -> openImageSelectionDialog(context));
    }

    private void openImageSelectionDialog(Context context) {
        ImageSelectionDialog imageSelectionDialog = new ImageSelectionDialog(context, this);
        imageSelectionDialog.show();
    }

    public void setExerciseImage(int resourceId) {
        exerciseImageView.setImageResource(resourceId);
    }

    public void show() {
        dialog.show();
    }
}