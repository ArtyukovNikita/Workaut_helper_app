package com.example.workauthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица категорий
    private static final String TABLE_CATEGORIES = "categories";
    private static final String COLUMN_CATEGORY_ID = "id";
    private static final String COLUMN_CATEGORY_NAME = "category";

    // Таблица упражнений
    private static final String TABLE_EXERCISES = "exercises";
    private static final String COLUMN_EXERCISE_ID = "id";
    private static final String COLUMN_EXERCISE_NAME = "exercise";
    private static final String COLUMN_EXERCISE_IMAGE = "image";
    private static final String COLUMN_EXERCISE_CATEGORY_ID = "category_id";

    // Таблица тренировок
    private static final String TABLE_WORKOUTS = "workouts";
    private static final String COLUMN_WORKOUT_ID = "id";
    private static final String COLUMN_WORKOUT_EXERCISE_ID = "exercise_id";
    private static final String COLUMN_WORKOUT_DATE = "date";
    private static final String COLUMN_WORKOUT_WEIGHT = "weight";
    private static final String COLUMN_WORKOUT_REPS = "reps";
    private static final String COLUMN_WORKOUT_SETS = "sets";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы категорий
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CATEGORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CATEGORY_NAME + " TEXT NOT NULL);";
        db.execSQL(createCategoriesTable);

        // Создание таблицы упражнений
        String createExercisesTable = "CREATE TABLE " + TABLE_EXERCISES + " (" +
                COLUMN_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EXERCISE_NAME + " TEXT NOT NULL, " +
                COLUMN_EXERCISE_IMAGE + " TEXT, " +
                COLUMN_EXERCISE_CATEGORY_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_EXERCISE_CATEGORY_ID + ") REFERENCES " +
                TABLE_CATEGORIES + "(" + COLUMN_CATEGORY_ID + "));";
        db.execSQL(createExercisesTable);

        // Создание таблицы тренировок
        String createWorkoutsTable = "CREATE TABLE " + TABLE_WORKOUTS + " (" +
                COLUMN_WORKOUT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORKOUT_EXERCISE_ID + " INTEGER, " +
                COLUMN_WORKOUT_DATE + " TEXT NOT NULL, " +
                COLUMN_WORKOUT_WEIGHT + " REAL, " +
                COLUMN_WORKOUT_REPS + " INTEGER, " +
                COLUMN_WORKOUT_SETS + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_WORKOUT_EXERCISE_ID + ") REFERENCES " +
                TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + "));";
        db.execSQL(createWorkoutsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    public void addExercise(String name, String image, int categoryId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, name);
        values.put(COLUMN_EXERCISE_IMAGE, image);
        values.put(COLUMN_EXERCISE_CATEGORY_ID, categoryId); // Добавляем ID категории

        db.insert(TABLE_EXERCISES, null, values);
        db.close();
    }

    public ArrayList<Exercise> getAllExercises() {
        ArrayList<Exercise> exerciseList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_EXERCISES, null);
        if (cursor.moveToFirst()) {
            do {
                // Получаем индексы столбцов
                int nameIndex = cursor.getColumnIndex(COLUMN_EXERCISE_NAME);
                int imageIndex = cursor.getColumnIndex(COLUMN_EXERCISE_IMAGE);

                // Проверяем, что индексы действительны
                if (nameIndex != -1 && imageIndex != -1) {
                    String name = cursor.getString(nameIndex);
                    String image = cursor.getString(imageIndex);
                    exerciseList.add(new Exercise(name, image)); // Создаем объект Exercise и добавляем его в список
                } else {
                    // Обработка ошибки: один из столбцов не найден
                    // Например, можно вывести сообщение в лог или игнорировать эту запись
                    System.err.println("Ошибка: один из столбцов не найден в таблице упражнений.");
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return exerciseList;
    }
}