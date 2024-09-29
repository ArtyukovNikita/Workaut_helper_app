package com.example.workauthelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;

public class CategoryActivity extends AppCompatActivity {

    private ListView listView;
    private String[] categories = {"Спина", "Ноги", "Грудь"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        listView = findViewById(R.id.category_list_view);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(CategoryActivity.this, ExerciseActivity.class);
                intent.putExtra("category", categories[position]);
                startActivity(intent);
            }
        });
    }
}
