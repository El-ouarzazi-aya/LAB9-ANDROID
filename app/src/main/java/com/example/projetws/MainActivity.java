package com.example.projetws;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnAdd, btnList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAdd  = findViewById(R.id.btnAdd);
        btnList = findViewById(R.id.btnList);

        btnAdd.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddEtudiant.class);
            startActivity(intent);
        });

        btnList.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ListEtudiant.class);
            startActivity(intent);
        });
    }
}