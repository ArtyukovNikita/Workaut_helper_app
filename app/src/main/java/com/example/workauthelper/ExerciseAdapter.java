package com.example.workauthelper;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exerciseList;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.exerciseList = exercises;
    }

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
        Button selectButton = convertView.findViewById(R.id.select_exercise_button);

        // Устанавливаем данные в элементы
        exerciseName.setText(exercise.getName());
        exerciseIcon.setImageResource(exercise.getIconResourceId());

        // Обработчик нажатия на кнопку меню
        popupMenuButton.setOnClickListener(v -> {
            showPopupMenu(v, exercise); // Передаем представление и упражнение
        });

        // Обработка нажатия на кнопку
        selectButton.setOnClickListener(v -> {
            // Логика выбора упражнения
            // Здесь можно добавить нужные действия при выборе упражнения
            // Например, можно добавить в тренировку или показать уведомление
            Toast.makeText(getContext(), "Выбрано упражнение: " + exercise.getName(), Toast.LENGTH_SHORT).show();
        });

        return convertView;
    }

    private void showPopupMenu(View view, Exercise exercise) {
        // Реализация всплывающего меню
        // Например, используйте PopupMenu для отображения действий с упражнением
    }
}
