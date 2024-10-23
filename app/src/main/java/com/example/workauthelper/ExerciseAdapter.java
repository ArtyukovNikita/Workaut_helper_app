package com.example.workauthelper;

import com.example.workauthelper.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.MenuRes;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exercises;

    public ExerciseAdapter(@NonNull Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.exercises = exercises;
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
            showPopupMenu(v, exercise); // Передаем представление и упражнение
        });

        return convertView;
    }

    private void showPopupMenu(View view, Exercise exercise) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.exercise_popup_menu, popupMenu.getMenu()); // Убедитесь, что R.menu правильный

        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.edit_ex) {
                editExercise(exercise);
                return true;
            } else if (id == R.id.delete_ex) {
                deleteExercise(exercise);
                return true;
            }
            return false;
        });


        popupMenu.show();
    }

    private void editExercise(Exercise exercise) {
        AddExerciseDialog dialog = new AddExerciseDialog(getContext(), exercise);
        dialog.show();
    }

    private void deleteExercise(Exercise exercise) {
        DatabaseHelper dbHelper = new DatabaseHelper(getContext());
        dbHelper.deleteExercise(exercise.getId()); // Передаем ID упражнения
        exercises.remove(exercise); // Удаляем из списка
        notifyDataSetChanged(); // Обновляем адаптер
    }

}
