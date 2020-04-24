package ru.ach4god.wirelessandroidmouse;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import ru.ach4god.wirelessandroidmouse.MouseServer.MouseServerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startServerActivity(View v){
        startActivity(new Intent(this, MouseServerActivity.class));
    }
}
