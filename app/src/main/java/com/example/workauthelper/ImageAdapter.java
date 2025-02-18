package com.example.workauthelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class ImageAdapter extends BaseAdapter {

    private Context context;
    private List<Integer> imageResourceIds;

    public ImageAdapter(Context context, List<Integer> imageResourceIds) {
        this.context = context;
        this.imageResourceIds = imageResourceIds;
    }

    @Override
    public int getCount() {
        return imageResourceIds.size();
    }

    @Override
    public Object getItem(int position) {
        return imageResourceIds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(context);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(200, 200)); // Установите размеры для изображений
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(imageResourceIds.get(position)); // Устанавливаем изображение

        return imageView;
    }
}