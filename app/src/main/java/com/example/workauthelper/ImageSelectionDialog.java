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

    public interface OnImageSelectedListener {
        void onImageSelected(int resourceId);
    }
    public ImageSelectionDialog(Context context, AddExerciseDialog addExerciseDialog) {
        this.addExerciseDialog = addExerciseDialog;
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_image_selection);


        GridView gridView = dialog.findViewById(R.id.image_grid_view);
        DatabaseHelper dbHelper = new DatabaseHelper(context);
        imagePaths = dbHelper.getAllVectorImages();

        ImageAdapter imageAdapter = new ImageAdapter(context, imagePaths);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int selectedImagePath = imagePaths.get(position);
                if (listener != null) {
                    listener.onImageSelected(selectedImagePath); // Уведомляем слушателя
                }
                dialog.dismiss();
            }
        });

        Button closeButton = dialog.findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> dialog.dismiss());
    }

    private OnImageSelectedListener listener;

    public void setOnImageSelectedListener(OnImageSelectedListener listener) {
        this.listener = listener;
    }

    public void show() {
        dialog.show();
    }

    // Вызовите этот метод, когда изображение будет выбрано
    private void notifyImageSelected(int resourceId) {
        if (listener != null) {
            listener.onImageSelected(resourceId);
        }
    }
}

