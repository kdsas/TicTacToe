package com.kwdevelopmentllc.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class CoverActivity extends AppCompatActivity {
    Button startButton, creditsButton,quitButton;
    ImageView gameLogoImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        startButton = (Button) findViewById(R.id.startButton);
        creditsButton = (Button) findViewById(R.id.creditsButton);
        quitButton = (Button) findViewById(R.id.quitButton);
        gameLogoImageView = (ImageView) findViewById(R.id.gameLogoImageView);
        startMusic();
        startButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoverActivity.this, StartActivity.class));
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });
        creditsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(CoverActivity.this, CreditsActivity.class));
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });
        quitButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finishAffinity();
                System.exit(0);


            }
        });


    }
    public void startMusic(){
        Intent intent = new Intent(CoverActivity.this, BackgroundMusic.class);
        startService(intent);

    }

}