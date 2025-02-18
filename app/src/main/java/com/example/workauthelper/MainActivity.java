package com.example.workauthelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private ImageButton calendarButton;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    public static final String PREFS_NAME = "MyPrefs";
    public static final String KEY_IMAGES_LOADED = "images_loaded";
    private View calendarLayout;
    private boolean isCalendarVisible = false;
    private Calendar currentCalendar;
    private TextView headerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация компонентов UI
        headerTextView = findViewById(R.id.hello_world);
        updateHeaderWithCurrentDate(); // Обновляем заголовок текущей датой

        currentCalendar = Calendar.getInstance();
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        menuButton = findViewById(R.id.menu_button);
        calendarButton = findViewById(R.id.calendar_button);

        // Обработка нажатия кнопки меню
        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Установка слушателя для обработки отступов в верхней части экрана
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Настройка навигационного меню
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Обработка выбора элемента в навигационном меню
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_exercises) {
                Intent intent = new Intent(this, CategoryActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });

        // Проверка, загружены ли изображения в базу данных
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean imagesLoaded = preferences.getBoolean(KEY_IMAGES_LOADED, false);
        if (!imagesLoaded) {
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.loadVectorImagesIntoDatabase(this); // Загружаем изображения
            preferences.edit().putBoolean(KEY_IMAGES_LOADED, true).apply();
        }

        // Инициализация календаря
        calendarLayout = getLayoutInflater().inflate(R.layout.calendar_layout, null);
        calendarLayout.setVisibility(View.GONE); // Скрываем календарь по умолчанию

        if (calendarLayout.getParent() != null) {
            ((ViewGroup) calendarLayout.getParent()).removeView(calendarLayout);
        }

        ((RelativeLayout) findViewById(R.id.main_content)).addView(calendarLayout);

        // Обработка нажатия кнопки для выбора даты
        calendarButton.setOnClickListener(v -> toggleCalendar());

        Button closeButton = calendarLayout.findViewById(R.id.btn_close_calendar);
        closeButton.setOnClickListener(v -> closeCalendar()); // Закрытие календаря

        updateCalendarTitle(); // Обновляем заголовок календаря

        // Обработка кнопок "предыдущий" и "следующий" в календаре
        ImageButton previousButton = calendarLayout.findViewById(R.id.btn_previous);
        ImageButton nextButton = calendarLayout.findViewById(R.id.btn_next);

        previousButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1); // Переход на предыдущий месяц
            setupCalendar();
            updateCalendarTitle();
        });

        nextButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1); // Переход на следующий месяц
            setupCalendar();
            updateCalendarTitle();
        });

        setupCalendar(); // Настройка календаря

        // Обработка нажатия кнопки добавления
        ImageButton addButton = findViewById(R.id.btn_add);
        addButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    // Метод для обновления заголовка текущей датой
    private void updateHeaderWithCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MM/yyyy", Locale.getDefault());
        String currentDate = sdf.format(calendar.getTime());
        headerTextView.setText(currentDate + " (Today)"); // Устанавливаем текст заголовка
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START); // Закрываем меню при нажатии кнопки "Назад"
        } else {
            super.onBackPressed();
        }
    }

    // Метод для переключения видимости календаря
    private void toggleCalendar() {
        if (isCalendarVisible) {
            closeCalendar();
        } else {
            openCalendar();
        }
    }

    // Метод для открытия календаря
    private void openCalendar() {
        calendarLayout.setVisibility(View.VISIBLE);
        calendarLayout.setTranslationY(-calendarLayout.getHeight());
        calendarLayout.animate()
                .translationY(0)
                .setDuration(300)
                .start();
        isCalendarVisible = true;
    }

    // Метод для закрытия календаря
    private void closeCalendar() {
        calendarLayout.animate()
                .translationY(-calendarLayout.getHeight())
                .setDuration(300)
                .withEndAction(() -> calendarLayout.setVisibility(View.GONE))
                .start();
        isCalendarVisible = false;
    }

    // Метод для обновления заголовка календаря
    private void updateCalendarTitle() {
        TextView calendarTitle = calendarLayout.findViewById(R.id.calendar_title);
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        calendarTitle.setText(sdf.format(currentCalendar.getTime())); // Устанавливаем заголовок календаря
    }

    // Метод для настройки календаря
    private void setupCalendar() {
        GridLayout calendarGrid = calendarLayout.findViewById(R.id.calendar_grid);
        calendarGrid.removeAllViews(); // Очищаем сетку календаря

        int daysInMonth = currentCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        currentCalendar.set(Calendar.DAY_OF_MONTH, 1); // Устанавливаем первый день месяца

        int firstDayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK); // Получаем первый день недели

        // Заполняем пустые ячейки до первого дня месяца
        for (int i = 1; i < firstDayOfWeek; i++) {
            Button emptyButton = new Button(this);
            emptyButton.setEnabled(false);
            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(0), GridLayout.spec(i - 1));
            params.width = 80;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(1, 1, 1, 1);
            emptyButton.setLayoutParams(params);
            emptyButton.setVisibility(View.INVISIBLE);
            calendarGrid.addView(emptyButton);
        }

        // Заполняем ячейки днями месяца
        for (int day = 1; day <= daysInMonth; day++) {
            final int dayOfMonth = day;
            Button dayButton = new Button(this);
            dayButton.setText(String.valueOf(day)); // Устанавливаем текст кнопки

            int row = (day + firstDayOfWeek - 2) / 7; // Определяем строку
            int col = (day + firstDayOfWeek - 2) % 7; // Определяем столбец

            if (row >= 6) {
                break; // Если превышает 6 строк, выходим из цикла
            }

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec(row), GridLayout.spec(col));
            params.width = 80;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.setMargins(1, 1, 1, 1);
            dayButton.setLayoutParams(params);

            // Обработка нажатия кнопки дня в календаре
            dayButton.setOnClickListener(v -> {
                // Обновляем заголовок с выбранной датой
                String selectedDate = dayOfMonth + " " + (currentCalendar.get(Calendar.MONTH) + 1) + "/" + currentCalendar.get(Calendar.YEAR);
                headerTextView.setText(selectedDate); // Обновляем заголовок с выбранной датой

                // Сохраняем выбранную дату в SharedPreferences для дальнейшего использования
                SharedPreferences sharedPreferences = getSharedPreferences("WorkoutHelper", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("selected_date", selectedDate);
                editor.apply();

            });

            calendarGrid.addView(dayButton); // Добавляем кнопку с днем в сетку
        }
    }
}
