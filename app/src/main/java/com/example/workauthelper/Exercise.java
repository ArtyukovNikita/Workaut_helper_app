package com.example.workauthelper;

public class Exercise {
    private String name;
    private int iconResourceId;

    public Exercise(String name, int iconResourceId) {
        this.name = name;
        this.iconResourceId = iconResourceId;
    }

    public String getName() {
        return name;
    }

    public int getIconResourceId() {
        return iconResourceId;
    }
}
