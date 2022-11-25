package com.example.memorygame.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.memorygame.AuthenticationActivity;
import com.example.memorygame.GlobalObject;
import com.example.memorygame.R;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button startButton = findViewById(R.id.button);
        startButton.setOnClickListener(view -> changeActivity());
        GlobalObject globalObject = GlobalObject.getInstance();
        globalObject.setGameState(1);
    }
    private void changeActivity(){
        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
    }
}