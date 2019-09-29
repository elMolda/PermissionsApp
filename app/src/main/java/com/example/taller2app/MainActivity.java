package com.example.taller2app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private ImageButton btnCmr;
    private ImageButton btnCnt;
    private ImageButton btnLct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnCmr = findViewById(R.id.btnCmr);
        btnCnt = findViewById(R.id.btnCnt);
        btnLct = findViewById(R.id.btnLct);

        btnCmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnCmr.getContext(), CameraActivity.class);
                startActivity(intent);
            }
        });

        btnCnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnCnt.getContext(), ContactsActivity.class);
                startActivity(intent);
            }
        });

        btnLct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(btnLct.getContext(), LocationActivity.class);
                startActivity(intent);
            }
        });
    }
}
