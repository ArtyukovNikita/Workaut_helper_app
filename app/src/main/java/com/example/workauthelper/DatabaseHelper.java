package com.example.workauthelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 2;

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

    // Таблица векторных изображений
    private static final String TABLE_VECTOR_IMAGES = "vector_images";
    private static final String COLUMN_IMAGE_ID = "id";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_IMAGE_EXERCISE_ID = "exercise_id";

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

        // Создание таблицы векторных изображений
        String createVectorImagesTable = "CREATE TABLE " + TABLE_VECTOR_IMAGES + " (" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                COLUMN_IMAGE_EXERCISE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_IMAGE_EXERCISE_ID + ") REFERENCES " +
                TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + "));";
        db.execSQL(createVectorImagesTable);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VECTOR_IMAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        onCreate(db);
    }

    // Метод для получения идентификаторов ресурсов drawable
    public List<Integer> getDrawableResourceIds(Context context) {
        List<Integer> drawableIds = new ArrayList<>();
        String packageName = context.getPackageName();

        // Получаем список всех ресурсов в папке drawable
        try {
            Field[] drawablesFields = R.drawable.class.getFields();
            for (Field field : drawablesFields) {
                String resourceName = field.getName();
                if (resourceName.startsWith("exc_")) {
                    // Получаем идентификатор ресурса и добавляем его в список
                    int resourceId = context.getResources().getIdentifier(resourceName, "drawable", packageName);
                    drawableIds.add(resourceId);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return drawableIds;
    }

    // Метод для автоматической загрузки векторных изображений в базу данных
    public void loadVectorImagesIntoDatabase(Context context) {
        List<Integer> drawableIds = getDrawableResourceIds(context);
        SQLiteDatabase db = this.getWritableDatabase();

        for (Integer drawableId : drawableIds) {
            String imagePath = String.valueOf(drawableId); // Можно использовать ID как путь или как идентификатор

            // Проверка на существование изображения в базе данных
            Cursor cursor = db.query(TABLE_VECTOR_IMAGES,
                    new String[]{COLUMN_IMAGE_PATH},
                    COLUMN_IMAGE_PATH + " = ?",
                    new String[]{imagePath},
                    null, null, null);

            // Если изображения нет в базе данных, вставляем его
            if (cursor.getCount() == 0) {
                ContentValues values = new ContentValues();
                values.put(COLUMN_IMAGE_PATH, imagePath);
                db.insert(TABLE_VECTOR_IMAGES, null, values);
            }

            cursor.close(); // Закрываем курсор после использования
        }

        db.close(); // Закрываем базу данных
    }


    public List<Integer> getAllVectorImages() {
        List<Integer> images = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_VECTOR_IMAGES,
                new String[]{COLUMN_IMAGE_PATH},
                null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int imagePath = cursor.getInt(0); // Получаем только идентификатор ресурса
                images.add(imagePath); // Добавляем идентификатор в список
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return images;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CATEGORY_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String categoryName = cursor.getString(0); // Получаем название категории
                categories.add(categoryName); // Добавляем название в список
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categories;
    }
    public void addCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }
    public void addExercise(String exerciseName, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, exerciseName);
        values.put(COLUMN_EXERCISE_CATEGORY_ID, category);
        db.insert(TABLE_EXERCISES, null, values);
        db.close();
    }

}