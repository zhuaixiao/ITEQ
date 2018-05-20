package com.example.iteq;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class About extends AppCompatActivity {
    private LinearLayout layout1;
    private LinearLayout layout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        layout1 = findViewById(R.id.linear1);
        layout2 = findViewById(R.id.linear2);
        String i = getIntent().getStringExtra("i");
        if (i.equals("i")) {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
    }
}
