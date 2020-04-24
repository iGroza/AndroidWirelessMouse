package ru.ach4god.wirelessandroidmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: create splash screen
        startActivity(new Intent(this, MainActivity.class));
    }
}
