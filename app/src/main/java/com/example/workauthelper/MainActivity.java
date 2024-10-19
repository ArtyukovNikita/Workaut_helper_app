package com.example.workauthelper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.google.android.material.navigation.NavigationView;
import android.content.Intent;
import android.content.SharedPreferences;

public class MainActivity extends AppCompatActivity {

    private ImageButton menuButton;
    private ImageButton calendarButton;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private static final String PREFS_NAME = "MyPrefs";
    private static final String KEY_IMAGES_LOADED = "images_loaded";
    private View calendarLayout;
    private boolean isCalendarVisible = false;
    private CustomSwipeRefreshLayout swipeRefreshLayout; // Используем кастомный SwipeRefreshLayout

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Инициализация DrawerLayout и NavigationView
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        // Установка обработчиков для кнопок
        menuButton = findViewById(R.id.menu_button);
        calendarButton = findViewById(R.id.calendar_button);

        // Обработчик нажатия на кнопку меню
        menuButton.setOnClickListener(v -> {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START);
            } else {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        // Установка отступов для шапки
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.header), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Настройка ActionBarDrawerToggle для работы с меню
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Установка обработчика для навигационного меню
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_exercises) {
                Intent intent = new Intent(this, CategoryActivity.class);
                startActivity(intent);
                drawerLayout.closeDrawer(GravityCompat.START);
            }
            return true;
        });

        // Получаем экземпляр DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Проверяем, были ли уже загружены изображения
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean imagesLoaded = preferences.getBoolean(KEY_IMAGES_LOADED, false);

        if (!imagesLoaded) {
            dbHelper.loadVectorImagesIntoDatabase(this);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_IMAGES_LOADED, true);
            editor.apply();
        }

        // Инициализация макета календаря
        calendarLayout = getLayoutInflater().inflate(R.layout.calendar_layout, null);
        calendarLayout.setVisibility(View.GONE); // Изначально скрыт

        // Установка обработчика для кнопки календаря
        calendarButton.setOnClickListener(v -> toggleCalendar());

        // Установка обработчика для кнопки закрытия календаря
        Button closeButton = calendarLayout.findViewById(R.id.btn_close_calendar);
        closeButton.setOnClickListener(v -> closeCalendar());

        // Добавление макета календаря в основной макет
        ((RelativeLayout) findViewById(R.id.main_content)).addView(calendarLayout);

        // Настройка сетки календаря
        setupCalendar();

        // Инициализация кастомного SwipeRefreshLayout
        swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setDrawerLayout(drawerLayout); // Установка DrawerLayout

        // Установка слушателя для SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(() -> {
            if (!isCalendarVisible) {
                openCalendar();
            }
            swipeRefreshLayout.setRefreshing(false); // Остановить анимацию
        });
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void toggleCalendar() {
        if (isCalendarVisible) {
            closeCalendar();
        } else {
            openCalendar();
        }
    }

    private void openCalendar() {
        calendarLayout.setVisibility(View.VISIBLE);
        isCalendarVisible = true;
    }

    private void closeCalendar() {
        calendarLayout.setVisibility(View.GONE);
        isCalendarVisible = false;
    }

    private void setupCalendar() {
        GridLayout calendarGrid = calendarLayout.findViewById(R.id.calendar_grid);
        calendarGrid.removeAllViews();

        // Заполнение сетки днями месяца
        for (int i = 1; i <= 30; i++) { // Упрощенно для 30 дней
            final int day = i; // Создаем финальную переменную
            Button dayButton = new Button(this);
            dayButton.setText(String.valueOf(day));
            dayButton.setOnClickListener(v -> {
                // Обработка нажатия на день
                Toast.makeText(this, "Выбран день: " + day, Toast.LENGTH_SHORT).show();
            });
            calendarGrid.addView(dayButton);
        }
    }
}
