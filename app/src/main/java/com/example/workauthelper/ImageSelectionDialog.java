package com.example.workauthelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Toast;
import android.widget.GridView;


import java.util.List;

public class ImageSelectionDialog {

    private Dialog dialog;
    private List<Integer> imagePaths; // Список путей к изображениям
    private AddExerciseDialog addExerciseDialog;


    public ImageSelectionDialog(Context context, AddExerciseDialog addExerciseDialog) {
        this.addExerciseDialog = addExerciseDialog;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_image_selection); // Создайте соответствующий layout для диалога выбора изображений

        GridView gridView = dialog.findViewById(R.id.image_grid_view); // Изменяем ListView на GridView

        // Получаем изображения из базы данных
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        imagePaths = dbHelper.getAllVectorImages(); // Метод, который возвращает список идентификаторов изображений

        // Создаем адаптер для отображения изображений
        ImageAdapter imageAdapter = new ImageAdapter(context, imagePaths);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Получаем выбранное изображение
                int selectedImagePath = imagePaths.get(position);
                // Устанавливаем изображение в AddExerciseDialog
                addExerciseDialog.setExerciseImage(selectedImagePath); // Преобразуем путь к ресурсу
                dialog.dismiss();
            }
        });
        // Внутри конструктора ImageSelectionDialog
        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss(); // Закрыть диалог
            }
        });

    }

    public interface OnImageSelectedListener {
        void onImageSelected(int resourceId);
    }


    private OnImageSelectedListener listener;

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }

    // Вызовите этот метод, когда изображение будет выбрано
    private void notifyImageSelected(int resourceId) {
        if (listener != null) {
            listener.onImageSelected(resourceId);
        }
    }


    public void show() {
        dialog.show();
    }
}
