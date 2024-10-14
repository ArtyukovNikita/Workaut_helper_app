package com.example.workauthelper;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
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
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Открытие бокового меню
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    drawerLayout.openDrawer(GravityCompat.START);
                }
            }
        });

        // Обработчик нажатия на кнопку календаря
        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Здесь будет код для открытия календаря
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
                try {
                    Intent intent = new Intent(this, CategoryActivity.class);
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace(); // Выводим ошибку в Logcat
                }
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });

        // Получаем экземпляр DatabaseHelper
        DatabaseHelper dbHelper = new DatabaseHelper(this);

        // Проверяем, были ли уже загружены изображения
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        boolean imagesLoaded = preferences.getBoolean(KEY_IMAGES_LOADED, false);

        if (!imagesLoaded) {
            // Если изображения еще не загружены, загружаем их в базу данных
            dbHelper.loadVectorImagesIntoDatabase(this);

            // Устанавливаем флаг, что изображения были загружены
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(KEY_IMAGES_LOADED, true);
            editor.apply();
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
