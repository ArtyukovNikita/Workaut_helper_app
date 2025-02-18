package com.example.workauthelper;

public class Exercise {
    private String name;
    private int iconResourceId;
    private int id; // Поле ID

    // Конструктор
    public Exercise(String name, int iconResourceId, int id) {
        this.name = name;
        this.iconResourceId = iconResourceId;
        this.id = id; // Сохраняем ID
    }

    public String getName() {
        return name;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }

    public int getId() {
        return id; // Метод для получения ID
    }
}
