package com.example.workauthelper;

public class Exercise {
    private String name;
    private String image; // Путь к изображению
    private int iconResourceId; // ID ресурса изображения

    // Конструктор для пути к изображению
    public Exercise(String name, String image) {
        this.name = name;
        this.image = image;
        this.iconResourceId = -1; // Устанавливаем значение по умолчанию
    }

    // Конструктор для ресурса изображения
    public Exercise(String name, int iconResourceId) {
        this.name = name;
        this.iconResourceId = iconResourceId;
        this.image = null; // Устанавливаем значение по умолчанию
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public int getIconResourceId() {
        return iconResourceId; // Возвращает ID ресурса изображения
    }
}