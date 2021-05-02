package com.kwdevelopmentllc.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CreditsActivity extends AppCompatActivity {
    Button buttonBack;
    TextView textViewCredits;
    ImageView imageViewLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        buttonBack= (Button) findViewById(R.id.buttonBack);
        textViewCredits= (TextView) findViewById(R.id.textViewCredits);
        imageViewLogo = (ImageView) findViewById(R.id.imageViewLogo);


       buttonBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreditsActivity.this, StartActivity.class));
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();

            }
        });




    }
}