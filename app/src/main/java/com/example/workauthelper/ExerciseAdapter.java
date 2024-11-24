package com.example.workauthelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ExerciseAdapter extends ArrayAdapter<Exercise> {
    private List<Exercise> exerciseList;
    private Context context;

    public ExerciseAdapter(Context context, List<Exercise> exercises) {
        super(context, 0, exercises);
        this.exerciseList = exercises;
        this.context = context;
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Exercise exercise = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_exercise, parent, false);
        }

        // Устанавливаем данные в элементы
        setupView(convertView, exercise);

        // Обработчик нажатия на кнопку меню
        ImageButton popupMenuButton = convertView.findViewById(R.id.popup_menu_button);
        popupMenuButton.setOnClickListener(v -> showPopupMenu(v, exercise));

        // Обработка нажатия на кнопку выбора
        Button selectButton = convertView.findViewById(R.id.select_exercise_button);
        selectButton.setOnClickListener(v -> addExerciseToWorkout(exercise));

        return convertView;
    }

    private void setupView(View convertView, Exercise exercise) {
        ImageView exerciseIcon = convertView.findViewById(R.id.exercise_icon);
        TextView exerciseName = convertView.findViewById(R.id.exercise_name);

        exerciseName.setText(exercise.getName());
        exerciseIcon.setImageResource(exercise.getIconResourceId());
    }

    private void showPopupMenu(View view, Exercise exercise) {
        PopupMenu popupMenu = new PopupMenu(getContext(), view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.exercise_popup_menu, popupMenu.getMenu());
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
        boolean success = dbHelper.deleteExercise(exercise.getId()); // Обработка результата удаления
        if (success) {
            exerciseList.remove(exercise); // Удаляем из списка
            notifyDataSetChanged(); // Обновляем адаптер
            Toast.makeText(context, "Упражнение удалено", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Ошибка при удалении упражнения", Toast.LENGTH_SHORT).show();
        }
    }

    private void addExerciseToWorkout(Exercise exercise) {
        if (context instanceof ExerciseActivity) {
            // Получаем workoutId из SharedPreferences или передаем его как параметр
            SharedPreferences sharedPreferences = context.getSharedPreferences("WorkoutHelper", Context.MODE_PRIVATE);
            int workoutId = sharedPreferences.getInt("workout_id", -1); // Получаем сохраненный workout_id

            // Проверяем, что workoutId валиден
            if (workoutId != -1) {
                ((ExerciseActivity) context).addExerciseToWorkout(workoutId, exercise.getId()); // Теперь передаем оба параметра
            } else {
                Toast.makeText(context, "Ошибка: тренировка не найдена", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
