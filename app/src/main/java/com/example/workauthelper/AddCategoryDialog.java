package com.example.workauthelper;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class AddCategoryDialog {

    private Dialog dialog;
    private EditText categoryNameEditText;
    private OnCategoryAddedListener listener;

    public interface OnCategoryAddedListener {
        void onCategoryAdded(String categoryName);
    }

    public AddCategoryDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_category);
        categoryNameEditText = dialog.findViewById(R.id.edit_category_name);

        Button cancelButton = dialog.findViewById(R.id.cancel_button);
        Button addButton = dialog.findViewById(R.id.add_button);

        cancelButton.setOnClickListener(v -> dialog.dismiss()); // Закрыть диалог

        addButton.setOnClickListener(v -> {
            String categoryName = categoryNameEditText.getText().toString();
            DatabaseHelper dbHelper = new DatabaseHelper(context);
            dbHelper.addCategory(categoryName); // Метод для добавления категории в базу данных
            if (listener != null) {
                listener.onCategoryAdded(categoryName); // Уведомить о добавлении категории
            }
            dialog.dismiss(); // Закрыть диалог
        });
    }

    public void setOnCategoryAddedListener(OnCategoryAddedListener listener) {
        this.listener = listener; // Устанавливаем слушателя
    }

    public void show() {
        dialog.show();
    }
}
