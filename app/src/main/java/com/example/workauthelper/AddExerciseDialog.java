package com.example.workauthelper;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
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
    private int selectedImageResourceId; // Объявляем переменную
    private Exercise exercise; // Добавьте это поле
    // Конструктор для создания нового упражнения
    public AddExerciseDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_exercise);
        exerciseNameEditText = dialog.findViewById(R.id.edit_exercise_name);
        exerciseImageView = dialog.findViewById(R.id.edit_exercise_image);
        categorySpinner = dialog.findViewById(R.id.category_spinner);

        DatabaseHelper dbHelper = new DatabaseHelper(context);
        List<String> categories = dbHelper.getAllCategories();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button addButton = dialog.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dialog.dismiss());

        addButton.setOnClickListener(v -> {
            String exerciseName = exerciseNameEditText.getText().toString();
            int selectedCategoryId = categorySpinner.getSelectedItemPosition() + 1;

            if (!exerciseName.isEmpty() && selectedImageResourceId != 0) {
                // Проверяем, редактируем ли мы существующее упражнение
                if (exercise != null) {
                    dbHelper.addExercise(exerciseName, selectedCategoryId, selectedImageResourceId, exercise.getId()); // Передаем ID упражнения
                } else {
                    dbHelper.addExercise(exerciseName, selectedCategoryId, selectedImageResourceId, 0); // Новый ID для нового упражнения
                }
                dialog.dismiss();
            } else {
                Toast.makeText(context, "Введите название упражнения и выберите изображение", Toast.LENGTH_SHORT).show();
            }
        });



        exerciseImageView.setOnClickListener(v -> openImageSelectionDialog(context));
    }

    // Конструктор для редактирования существующего упражнения
    public AddExerciseDialog(Context context, Exercise exercise) {
        this(context); // Вызов существующего конструктора
        this.exercise = exercise; // Сохраняем ссылку на упражнение
        exerciseNameEditText.setText(exercise.getName());
        exerciseImageView.setImageResource(exercise.getIconResourceId());
        // Установите выбранное изображение и категорию, если нужно
        this.selectedImageResourceId = exercise.getIconResourceId(); // Сохраняем выбранный ID
    }

    private void openImageSelectionDialog(Context context) {
        ImageSelectionDialog imageSelectionDialog = new ImageSelectionDialog(context, this);
        imageSelectionDialog.setOnImageSelectedListener(resourceId -> {
            setExerciseImage(resourceId);
            this.selectedImageResourceId = resourceId; // Сохраняем выбранный идентификатор изображения
            Log.d("AddExerciseDialog", "Image Selected Resource ID: " + selectedImageResourceId); // Логируем выбранный ID
        });
        imageSelectionDialog.show();
    }

    public void setExerciseImage(int resourceId) {
        exerciseImageView.setImageResource(resourceId);
    }

    public void show() {
        dialog.show();
    }
}
