package com.kwdevelopmentllc.tictactoe;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;


public class OnlineGameActivity extends AppCompatActivity {
    TextView connectionText, playerNameText,retrievalTextView;
    ListView turnText;
    OnlineGameActivity.UdpClientHandler udpClientHandler;
    UdpClientThread udpClientThread;
    String ipAddress = "192.168.1.213";
    Integer port = 39000;
    Button slot, Backbutton, buttonChat;
    int activePlayer = 0, tappedSlot;
    boolean gameActive = true, startFlag = false;
    int[] gameState = {2, 2, 2, 2, 2, 2, 2, 2, 2};
    int[][] winningPositions = {{0, 1, 2}, {3, 4, 5}, {6, 7, 8}, {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, {0, 4, 8}, {2, 4, 6}};
    public static int counter = 0;
    public static final int CONNECTION_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 15000;
    String turnMess;
    String turnTextRetrieval;
    String playerName;
    String winningName;
    String winningNameRetrieval;
    ArrayAdapter arrayAdapter;
    ArrayList array_list;
    private InterstitialAd interstitialAd;
    private final String TAG = OnlineGameActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        playerNameText = (TextView) findViewById(R.id.playerNameText);
        retrievalTextView = (TextView) findViewById(R.id.retrievalTextView);
        Backbutton = (Button) findViewById(R.id.Backbutton);
        buttonChat = (Button) findViewById(R.id.buttonChat);
        turnText = (ListView) findViewById(R.id.turnText);
        String name = getIntent( ).getStringExtra("myName");
        playerNameText.setText(name);
        playerName = name;
        connectionText = (TextView) findViewById(R.id.connectionText);
        array_list = new ArrayList();
        // Initialize the Audience Network SDK
        AudienceNetworkAds.initialize(this);
        interstitialAd = new InterstitialAd(this, "301912531524676_301948074854455");
        //AdSettings.addTestDevice("8c15c671-3f3c-449a-85fb-ce33c6fbe5fa");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {


            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }



            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown

        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
       Backbutton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(OnlineGameActivity.this, StartActivity.class));


            }
        });
        buttonChat.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FragmentTransaction F_T =getSupportFragmentManager().beginTransaction();
                F_T.replace(R.id.container, new ChatFragment());
                F_T.commit();
                Backbutton.setVisibility(View.INVISIBLE);
                buttonChat.setVisibility(View.INVISIBLE);

            }
        });
        if (isNetworkConnected( )) {
            new ReadTurnTask( ).execute("http://kwdevgames.com/tictactoeturnread.php");
            new ReadWinTask( ).execute("http://kwdevgames.com/tictactoewinread.php");
        }
        udpClientHandler = new OnlineGameActivity.UdpClientHandler(this);
        udpClientThread = new UdpClientThread(
                ipAddress,
                port,
                udpClientHandler
        );
        udpClientThread.start( );//start udp client

    }

    private void updateState(String state) {
        connectionText.setText(state);

    }

    private void updateRxMsg(String rxmsg) {
        connectionText.append(rxmsg + "\n");
    }// display if connected to udp client or not

    private void clientEnd() {
        udpClientThread = null;
        connectionText.setText("not connected");


    }

    public static class UdpClientHandler extends Handler {
        public static final int UPDATE_STATE = 0;
        public static final int UPDATE_MSG = 1;
        public static final int UPDATE_END = 2;
        private OnlineGameActivity parent;

        public UdpClientHandler(OnlineGameActivity parent) {
            super( );
            this.parent = parent;
        }

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case UPDATE_STATE:
                    parent.updateState((String) msg.obj);
                    break;
                case UPDATE_MSG:
                    parent.updateRxMsg((String) msg.obj);
                    break;
                case UPDATE_END:
                    parent.clientEnd( );
                    break;
                default:
                    super.handleMessage(msg);
            }

        }
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo( ) != null;
    }

    private class ReadTurnTask extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con = (HttpURLConnection) url.openConnection( );
                //get InputStream instance
                InputStream is = con.getInputStream( );
                //create BufferedReader object
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                //read content of the file line by line



                String line;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                   turnTextRetrieval+=line;
                    ArrayList<String>
                            list = new ArrayList<String>();

                    // Populate the ArrayList
                    list.add(line);




                    Collections.sort(list, Collections.reverseOrder());
                    turnTextRetrieval= String.valueOf(list);

                }

                br.close();




            } catch (Exception e) {
                e.printStackTrace( );
                //close dialog if error occurs
            }

            return null;

        }

    }
    private class ReadWinTask extends AsyncTask<String, Integer, Void> {

        protected Void doInBackground(String... params) {
            URL url;
            try {
                //create url object to point to the file location on internet
                url = new URL(params[0]);
                //make a request to server
                HttpURLConnection con = (HttpURLConnection) url.openConnection( );
                //get InputStream instance
                InputStream is = con.getInputStream( );
                //create BufferedReader object
                BufferedReader br = new BufferedReader(new InputStreamReader(is));

                //read content of the file line by line



                String line;
                //read content of the file line by line
                while((line=br.readLine())!=null){
                    winningNameRetrieval+=line;
                    ArrayList<String>
                            list = new ArrayList<String>();

                    // Populate the ArrayList
                    list.add(line);




                    Collections.sort(list, Collections.reverseOrder());
                    winningNameRetrieval= String.valueOf(list);

                }

                br.close();




            } catch (Exception e) {
                e.printStackTrace( );
                //close dialog if error occurs
            }

            return null;

        }

    }


    public void dropIn(View view) {

        slot = (Button) view;
        final MediaPlayer mp = MediaPlayer.create(this, R.raw.blop_mark_diangelo);
        mp.start();
        tappedSlot = Integer.parseInt(slot.getTag( ).toString( ));
        String msg = ":" + slot.getTag( ).toString( );
        if (udpClientThread != null) {
            udpClientThread = new UdpClientThread(
                    msg,
                    udpClientHandler

            );
            if (gameState[tappedSlot] == 2) {
                // increase the counter
                // after every tap
                counter++;

                // check if its the last box
                if (counter == 9) {
                    // reset the game
                    gameActive = false;

                }

                // mark this position
                gameState[tappedSlot] = activePlayer;
                slot.setTranslationY(-200f);
                slot.animate( ).translationYBy(200f).setDuration(300);
                if (activePlayer == 0) {
                    // set the image of x
                    slot.setBackgroundColor(getResources( ).getColor(R.color.black));
                    slot.setText("x");
                    activePlayer = 1;
                    turnMess = playerName + ":" + "o's Turn - Tap to play";
                    retrievalTextView.setText(turnTextRetrieval);

                    if (udpClientThread != null) {

                        udpClientThread.Messenger = turnMess;
                    }
                    array_list.add(retrievalTextView.getText().toString());
                    arrayAdapter = new ArrayAdapter(OnlineGameActivity.this, android.R.layout.simple_list_item_1, array_list);
                    turnText.setAdapter(arrayAdapter);
                    new AsyncTurnText( ).execute(turnMess);
                    retrievalTextView.setText(turnTextRetrieval);
                    // change the status

                } else {
                    // set the image of o
                    slot.setBackgroundColor(getResources( ).getColor(R.color.purple_500));
                    slot.setText("o");
                    activePlayer = 0;
                    turnMess = playerName + ":" + "x's Turn - Tap to play";

                    if (udpClientThread != null) {

                        udpClientThread.Messenger = turnMess;
                    }
                    array_list.add(retrievalTextView.getText().toString());
                    arrayAdapter = new ArrayAdapter(OnlineGameActivity.this, android.R.layout.simple_list_item_1, array_list);
                    turnText.setAdapter(arrayAdapter);
                    retrievalTextView.setText(turnTextRetrieval);
                    new AsyncTurnText( ).execute(turnMess);

                }

            } else {

                Log.i("INFO", "UDPClient null");
            }

            int flag = 0;
            // Check if any player has won
            String winnerStr = null;
            for (int[] winningPosition : winningPositions) {
                if (gameState[winningPosition[0]] == gameState[winningPosition[1]] &&
                        gameState[winningPosition[1]] == gameState[winningPosition[2]] &&
                        gameState[winningPosition[0]] != 2) {

                    flag = 1;

                    // Somebody has won! - Find out who!

                    gameActive = false;
                    if (gameState[winningPosition[0]] == 0) {
                        winnerStr = playerName + ":" + "x has won";
                        winningName=winnerStr;
                        new AsyncWinText( ).execute(winningName);
                        if (udpClientThread != null) {

                            udpClientThread.Messenger = winnerStr;
                        }
                    } else {
                        winnerStr = playerName + ":" + "o has won";
                        winningName=winnerStr;
                        new AsyncWinText( ).execute(winningName);
                        if (udpClientThread != null) {

                            udpClientThread.Messenger = winnerStr;
                        }
                    }

                    if (counter == 9 && flag == 0) {
                        winnerStr = "Match Draw";
                        winningName=winnerStr;
                        new AsyncWinText( ).execute(winningName);
                        if (udpClientThread != null) {

                            udpClientThread.Messenger = winnerStr;
                        }


                    }

                    TextView winnerTextView = findViewById(R.id.winnerTextView);
                    winnerTextView.setVisibility(View.VISIBLE);
                    winnerTextView.setText(winningNameRetrieval);
                    //return;
                }
            }
        }
    }

    private class AsyncTurnText extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(OnlineGameActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute( );


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://kwdevgames.com/tictactoeturnsend.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace( );
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection( );
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder( )
                        .appendQueryParameter("turntext", turnMess);
                String query = builder.build( ).getEncodedQuery( );

                // Open connection for sending data
                OutputStream os = conn.getOutputStream( );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush( );
                writer.close( );
                os.close( );
                conn.connect( );

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace( );
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode( );

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream( );
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder( );
                    String line;

                    while ((line = reader.readLine( )) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString( ));

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace( );
                return "exception";
            } finally {
                conn.disconnect( );
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            //pdLoading.dismiss();

            if (result.equalsIgnoreCase("true")) {


                Toast.makeText(OnlineGameActivity.this, turnMess, Toast.LENGTH_LONG).show( );

            } else if (result.equalsIgnoreCase("false")) {

                // If there are internet problems display an error message
                Toast.makeText(OnlineGameActivity.this, "Problem Retrieving Turn Info", Toast.LENGTH_LONG).show( );

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(OnlineGameActivity.this, "Oh No! Internet Problem.", Toast.LENGTH_LONG).show( );

            }
        }
    }
    private class AsyncWinText extends AsyncTask<String, String, String> {
        //ProgressDialog pdLoading = new ProgressDialog(OnlineGameActivity.this);
        HttpURLConnection conn;
        URL url = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute( );


        }

        @Override
        protected String doInBackground(String... params) {
            try {

                // Enter URL address where your php file resides
                url = new URL("http://kwdevgames.com/tictactoewinsend.php");

            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace( );
                return "exception";
            }
            try {
                // Setup HttpURLConnection class to send and receive data from php and mysql
                conn = (HttpURLConnection) url.openConnection( );
                conn.setReadTimeout(READ_TIMEOUT);
                conn.setConnectTimeout(CONNECTION_TIMEOUT);
                conn.setRequestMethod("POST");

                // setDoInput and setDoOutput method depict handling of both send and receive
                conn.setDoInput(true);
                conn.setDoOutput(true);

                // Append parameters to URL
                Uri.Builder builder = new Uri.Builder( )
                        .appendQueryParameter("wintext", winningName);
                String query = builder.build( ).getEncodedQuery( );

                // Open connection for sending data
                OutputStream os = conn.getOutputStream( );
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(query);
                writer.flush( );
                writer.close( );
                os.close( );
                conn.connect( );

            } catch (IOException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace( );
                return "exception";
            }

            try {

                int response_code = conn.getResponseCode( );

                // Check if successful connection made
                if (response_code == HttpURLConnection.HTTP_OK) {

                    // Read data sent from server
                    InputStream input = conn.getInputStream( );
                    BufferedReader reader = new BufferedReader(new InputStreamReader(input));
                    StringBuilder result = new StringBuilder( );
                    String line;

                    while ((line = reader.readLine( )) != null) {
                        result.append(line);
                    }

                    // Pass data to onPostExecute method
                    return (result.toString( ));

                } else {

                    return ("unsuccessful");
                }

            } catch (IOException e) {
                e.printStackTrace( );
                return "exception";
            } finally {
                conn.disconnect( );
            }


        }

        @Override
        protected void onPostExecute(String result) {

            //this method will be running on UI thread

            //pdLoading.dismiss();

            if (result.equalsIgnoreCase("true")) {


                Toast.makeText(OnlineGameActivity.this, winningName, Toast.LENGTH_LONG).show( );

            } else if (result.equalsIgnoreCase("false")) {

                // If there are internet problems display an error message
                Toast.makeText(OnlineGameActivity.this, "Problem Retrieving Win Info", Toast.LENGTH_LONG).show( );

            } else if (result.equalsIgnoreCase("exception") || result.equalsIgnoreCase("unsuccessful")) {

                Toast.makeText(OnlineGameActivity.this, "Oh No! Internet Problem.", Toast.LENGTH_LONG).show( );

            }
        }
    }
}














