package com.example.workauthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {

    public ExerciseAdapter(@NonNull Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Получаем элемент данных для этой позиции
        Exercise exercise = getItem(position);

        // Проверяем, существует ли уже представление
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_exercise, parent, false);
        }

        // Получаем ссылки на элементы интерфейса
        ImageView exerciseIcon = convertView.findViewById(R.id.exercise_icon);
        TextView exerciseName = convertView.findViewById(R.id.exercise_name);
        ImageButton popupMenuButton = convertView.findViewById(R.id.popup_menu_button);

        // Устанавливаем данные в элементы
        exerciseName.setText(exercise.getName());
        exerciseIcon.setImageResource(exercise.getIconResourceId());

        // Обработчик нажатия на кнопку меню
        popupMenuButton.setOnClickListener(v -> {
            // Здесь вы можете реализовать popup меню
        });

        return convertView;
    }
}
