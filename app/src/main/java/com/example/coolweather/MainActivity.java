package com.example.coolweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAG", "onCreate:");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
