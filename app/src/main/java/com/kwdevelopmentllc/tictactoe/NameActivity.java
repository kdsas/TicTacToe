package com.kwdevelopmentllc.tictactoe;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;




public class NameActivity extends AppCompatActivity {
    EditText nameText;
    Button enterButton;
    String text="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nameText = (EditText) findViewById(R.id.nameText);
        enterButton = (Button) findViewById(R.id.enterButton);
        nameText.getText( ).toString( );
        enterButton.setOnClickListener(new View.OnClickListener( ) {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(NameActivity.this, OnlineGameActivity.class));
                String name = nameText.getText( ).toString( );
                Intent intent = new Intent(NameActivity.this, OnlineGameActivity.class);
                intent.putExtra("myName", name);
                startActivity(intent);
                PrefConfig.saveName(getApplicationContext(), name);
                final MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.blop_mark_diangelo);
                mp.start();
            }
        });

        //Determine screen size
        if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            text="LargeScreen";
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            text="MediumScreen";
        }
        else if ((getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            text="SmallScreen";
        }
        else {
            text="None";
        }

        //Determine density
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        float dpWidth=metrics.widthPixels/density;
        float dpHeight=metrics.heightPixels/density;

        text=text + " with density of "+metrics.densityDpi+ " which is a multiplier of "+density+", width of "+dpWidth+"dp, height of "+dpHeight+"dp";



    }



}