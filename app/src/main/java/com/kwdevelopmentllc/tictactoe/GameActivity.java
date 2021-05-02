package com.kwdevelopmentllc.tictactoe;
import android.content.DialogInterface;
import android.content.Intent;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;


import android.widget.Button;
import android.widget.TextView;



public class GameActivity extends AppCompatActivity implements View.OnClickListener {


    private Button[][] buttons = new Button[3][3];
    TextView textView;
    boolean gameActive = true;
    Button Back;

    int activePlayer = 0;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};


    int[][] winPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8},
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
            {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        textView = (TextView) findViewById(R.id.textView);
        Back = (Button) findViewById(R.id.Back);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(GameActivity.this, StartActivity.class));


            }
        });
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                String buttonID = "button_" + i + j;
                int resID = getResources( ).getIdentifier(buttonID, "id", getPackageName( ));
                buttons[i][j] = findViewById(resID);
                buttons[i][j].setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Button img = (Button) v;
        int tappedImage = Integer.parseInt(img.getTag( ).toString( ));
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.blop_mark_diangelo);
        mp.start();
        // game reset function will be called
        // if someone wins or the boxes are full
        if (!gameActive) {
            gameReset( );
        }


        if (gameState[tappedImage] == 2) {
            // increase the counter
            // after every tap
            counter++;

            // check if its the last box
            if (counter == 9) {
                // reset the game
                gameActive = false;

            }

            // mark this position
            gameState[tappedImage] = activePlayer;

            // this will give a motion
            // effect to the button
            img.setTranslationY(-1000f);

            // change the active player
            // from 0 to 1 or 1 to 0
            if (activePlayer == 0) {
                // set the image of x
                ((Button) v).setBackgroundColor(getResources( ).getColor(R.color.black));
                ((Button) v).setText("x");
                activePlayer = 1;


                // change the status
                textView.setText("o's Turn - Tap to play");
            } else {
                // set the image of o
                ((Button) v).setBackgroundColor(getResources( ).getColor(R.color.purple_500));
                ((Button) v).setText("o");
                activePlayer = 0;


                // change the status
                textView.setText("x's Turn - Tap to play");
            }
            img.animate( ).translationYBy(1000f).setDuration(300);
        }
        int flag = 0;
        // Check if any player has won
        String winnerStr = null;
        for (int[] winPosition : winPositions) {
            if (gameState[winPosition[0]] == gameState[winPosition[1]] &&
                    gameState[winPosition[1]] == gameState[winPosition[2]] &&
                    gameState[winPosition[0]] != 2) {
                flag = 1;

                // Somebody has won! - Find out who!

                // game reset function be called
                gameActive = false;
                if (gameState[winPosition[0]] == 0) {
                    winnerStr = "x has won";
                } else {
                    winnerStr = "o has won";
                }



                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Game Over" + '\n' + winnerStr)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener( ) {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(GameActivity.this, StartActivity.class));
                            }
                        });
                AlertDialog alert = builder.create( );
                alert.show( );

                // set the status if the match draw
                if (counter == 9 && flag == 0) {

                    textView.setText("Match Draw");
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                    builder1.setMessage("Game Over" + '\n' + winnerStr)
                            .setCancelable(false)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener( ) {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(GameActivity.this, StartActivity.class));
                                }
                            });
                    AlertDialog alert1 = builder.create( );
                    alert1.show( );


                }
            }
        }
    }





    // reset the game
    public void gameReset() {
        gameActive = true;
        activePlayer = 0;
        for (int i = 0; i < gameState.length; i++) {
            gameState[i] = 2;


        }




       textView.setText("x's Turn - Tap to play");
    }

}





