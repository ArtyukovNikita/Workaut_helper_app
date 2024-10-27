package com.example.workauthelper;

import static com.example.workauthelper.MainActivity.KEY_IMAGES_LOADED;
import static com.example.workauthelper.MainActivity.PREFS_NAME;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "fitness.db";
    private static final int DATABASE_VERSION = 4;

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
    private static final String COLUMN_WORKOUT_DATE = "date";

    // Таблица связи тренировок и упражнений
    private static final String TABLE_WORKOUT_EXERCISES = "workout_exercises";
    private static final String COLUMN_WORKOUT_EXERCISE_ID = "id";
    private static final String COLUMN_WORKOUT_EXERCISE_WORKOUT_ID = "workout_id";
    private static final String COLUMN_WORKOUT_EXERCISE_EXERCISE_ID = "exercise_id";

    // Таблица подходов
    private static final String TABLE_SETS = "sets";
    private static final String COLUMN_SET_ID = "id";
    private static final String COLUMN_SET_WORKOUT_EXERCISE_ID = "workout_exercise_id";
    private static final String COLUMN_SET_WEIGHT = "weight";
    private static final String COLUMN_SET_REPS = "reps";

    // Таблица векторных изображений
    private static final String TABLE_VECTOR_IMAGES = "vector_images";
    private static final String COLUMN_IMAGE_ID = "id";
    private static final String COLUMN_IMAGE_PATH = "image_path";
    private static final String COLUMN_IMAGE_EXERCISE_ID = "exercise_id";

    private Context context; // Добавьте это поле

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context; // Сохраните контекст
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
                COLUMN_WORKOUT_DATE + " TEXT NOT NULL);";
        db.execSQL(createWorkoutsTable);

        // Создание таблицы связи тренировок и упражнений
        String createWorkoutExercisesTable = "CREATE TABLE " + TABLE_WORKOUT_EXERCISES + " (" +
                COLUMN_WORKOUT_EXERCISE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " INTEGER, " +
                COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + ") REFERENCES " +
                TABLE_WORKOUTS + "(" + COLUMN_WORKOUT_ID + "), " +
                "FOREIGN KEY(" + COLUMN_WORKOUT_EXERCISE_EXERCISE_ID + ") REFERENCES " +
                TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + "));";
        db.execSQL(createWorkoutExercisesTable);

        // Создание таблицы подходов
        String createSetsTable = "CREATE TABLE " + TABLE_SETS + " (" +
                COLUMN_SET_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_SET_WORKOUT_EXERCISE_ID + " INTEGER, " +
                COLUMN_SET_WEIGHT + " REAL, " +
                COLUMN_SET_REPS + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_SET_WORKOUT_EXERCISE_ID + ") REFERENCES " +
                TABLE_WORKOUT_EXERCISES + "(" + COLUMN_WORKOUT_EXERCISE_ID + "));";
        db.execSQL(createSetsTable);

        // Создание таблицы векторных изображений
        String createVectorImagesTable = "CREATE TABLE " + TABLE_VECTOR_IMAGES + " (" +
                COLUMN_IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                COLUMN_IMAGE_EXERCISE_ID + " INTEGER, " +
                "FOREIGN KEY(" + COLUMN_IMAGE_EXERCISE_ID + ") REFERENCES " +
                TABLE_EXERCISES + "(" + COLUMN_EXERCISE_ID + "));";
        db.execSQL(createVectorImagesTable);

        //loadVectorImagesIntoDatabase(context);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SETS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUT_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WORKOUTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VECTOR_IMAGES);
        onCreate(db);

        // Сбросьте значение в SharedPreferences
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit().putBoolean(KEY_IMAGES_LOADED, false).apply();
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
                Log.d("DatabaseHelper", "Found image: " + imagePath); // Логируем найденные изображения
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return images;
    }

    public List<String> getAllCategories() {
        List<String> categories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CATEGORIES, new String[]{COLUMN_CATEGORY_ID, COLUMN_CATEGORY_NAME}, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                int categoryId = cursor.getInt(0); // Получаем ID категории
                String categoryName = cursor.getString(1); // Получаем название категории
                Log.d("DatabaseHelper", "Category ID: " + categoryId + ", Name: " + categoryName);
                categories.add(categoryName); // Добавляем название в список
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return categories;
    }



    public List<Exercise> getExercisesByCategory(int categoryId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_EXERCISES,
                new String[]{COLUMN_EXERCISE_ID, COLUMN_EXERCISE_NAME, COLUMN_EXERCISE_IMAGE}, // Добавьте COLUMN_EXERCISE_ID
                COLUMN_EXERCISE_CATEGORY_ID + " = ?",
                new String[]{String.valueOf(categoryId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int exerciseId = cursor.getInt(0); // Получаем ID упражнения
                String exerciseName = cursor.getString(1);
                int exerciseImage = cursor.getInt(2);
                exercises.add(new Exercise(exerciseName, exerciseImage, exerciseId)); // Передаем ID в конструктор
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return exercises;
    }


    public void addCategory(String categoryName) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_CATEGORY_NAME, categoryName);
        db.insert(TABLE_CATEGORIES, null, values);
        db.close();
    }
    public void addExercise(String exerciseName, int categoryId, int imageId, int exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EXERCISE_NAME, exerciseName);
        values.put(COLUMN_EXERCISE_CATEGORY_ID, categoryId);
        values.put(COLUMN_EXERCISE_IMAGE, imageId);

        // Если exerciseId больше 0, обновляем существующее упражнение
        if (exerciseId > 0) {
            db.update(TABLE_EXERCISES, values, COLUMN_EXERCISE_ID + " = ?", new String[]{String.valueOf(exerciseId)});
        } else {
            db.insert(TABLE_EXERCISES, null, values);
        }
        db.close();
    }

    public boolean deleteExercise(int exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsAffected = db.delete(TABLE_EXERCISES, COLUMN_EXERCISE_ID + " = ?", new String[]{String.valueOf(exerciseId)});
        db.close();
        return rowsAffected > 0; // Возвращаем true, если удаление прошло успешно
    }


    public int getCategoryIdByName(String categoryName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES,
                new String[]{COLUMN_CATEGORY_ID},
                COLUMN_CATEGORY_NAME + " = ?",
                new String[]{categoryName},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(0);
            cursor.close();
            return id;
        }
        return -1; // Возвращаем -1, если категория не найдена
    }

    public long getWorkoutIdByDate(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_WORKOUTS,
                new String[]{COLUMN_WORKOUT_ID},
                COLUMN_WORKOUT_DATE + " = ?",
                new String[]{date},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            long id = cursor.getLong(0);
            cursor.close();
            return id;
        }
        cursor.close();
        return -1; // Если тренировка не найдена
    }


    public long addWorkout(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_DATE, date);
        long id = db.insert(TABLE_WORKOUTS, null, values);
        db.close();
        return id; // Возвращаем ID новой тренировки
    }

    public void addExerciseToWorkout(int workoutId, int exerciseId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_WORKOUT_EXERCISE_WORKOUT_ID, workoutId);
        values.put(COLUMN_WORKOUT_EXERCISE_EXERCISE_ID, exerciseId);
        db.insert(TABLE_WORKOUT_EXERCISES, null, values);
        db.close();
    }

    public List<Exercise> getExercisesByWorkoutId(int workoutId) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WORKOUT_EXERCISES,
                new String[]{COLUMN_WORKOUT_EXERCISE_EXERCISE_ID},
                COLUMN_WORKOUT_EXERCISE_WORKOUT_ID + " = ?",
                new String[]{String.valueOf(workoutId)},
                null, null, null);

        while (cursor.moveToNext()) {
            int exerciseId = cursor.getInt(0);
            Exercise exercise = getExerciseById(exerciseId); // Метод для получения упражнения по ID
            exercises.add(exercise);
        }
        cursor.close();
        db.close();
        return exercises;
    }

    private Exercise getExerciseById(int exerciseId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_EXERCISES,
                new String[]{COLUMN_EXERCISE_NAME, COLUMN_EXERCISE_IMAGE},
                COLUMN_EXERCISE_ID + " = ?",
                new String[]{String.valueOf(exerciseId)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            String name = cursor.getString(0);
            int image = cursor.getInt(1);
            cursor.close();
            return new Exercise(name, image, exerciseId); // Возвращаем объект Exercise
        }
        cursor.close();
        return null; // Если упражнение не найдено
    }


}