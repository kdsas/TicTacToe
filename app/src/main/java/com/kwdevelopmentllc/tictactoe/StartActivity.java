package com.kwdevelopmentllc.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class StartActivity extends AppCompatActivity {
    Button singleButton, multiplayButton,backButton1;
    ImageView gameLogoImageView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        singleButton = (Button) findViewById(R.id.singleButton);
        multiplayButton = (Button) findViewById(R.id.multiplayButton);
        backButton1 = (Button) findViewById(R.id.backButton1);
        gameLogoImageView2 = (ImageView) findViewById(R.id.gameLogoImageView2);
        singleButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, GameActivity.class));
              final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });
        multiplayButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, NameActivity.class));
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });
        backButton1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartActivity.this, CoverActivity.class));
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });

    }




    }
