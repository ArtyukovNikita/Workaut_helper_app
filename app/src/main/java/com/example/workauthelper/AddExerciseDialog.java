package com.example.workauthelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class AddExerciseDialog {

    private Dialog dialog;
    private EditText exerciseNameEditText;
    private ImageView exerciseImageView;

    public AddExerciseDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_exercise); // Убедитесь, что вы создали файл dialog_add_exercise.xml
        exerciseNameEditText = dialog.findViewById(R.id.edit_exercise_name);
        exerciseImageView = dialog.findViewById(R.id.edit_exercise_image);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button addButton = dialog.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Закрыть диалог
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseName = exerciseNameEditText.getText().toString();
                // Здесь вы можете добавить код для добавления нового упражнения
                dialog.dismiss(); // Закрыть диалог
            }
        });

        // Обработчик нажатия на изображение
        exerciseImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelectionDialog(context);
            }
        });
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