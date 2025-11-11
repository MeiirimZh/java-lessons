package com.example.javalessons;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class Lecture extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lecture);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.createDatabaseIfNotExists();

        List<String> lectures = dbHelper.getLectureTitles();

        int lecture_id = getIntent().getIntExtra("lecture_id", -1);
        TextView lecture_title_text = findViewById(R.id.lecture_title_text);

        if (lecture_id != -1) {
            lecture_title_text.setText(lectures.get(lecture_id));
        }
        else {
            lecture_title_text.setText("Лекция не найдена");
        }
    }
}