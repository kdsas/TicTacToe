package com.kwdevelopmentllc.tictactoe;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.facebook.ads.*;
import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;
import static com.facebook.ads.AdSettings.DEBUG;



public class NameActivity extends AppCompatActivity implements AudienceNetworkAds.InitListener{
    EditText nameText;
    Button enterButton;
   private AdView adView;

    static void initialize(Context context) {
        if (!AudienceNetworkAds.isInitialized(context)) {
            if (DEBUG) {
                AdSettings.turnOnSDKDebugger(context);
            }

            AudienceNetworkAds
                    .buildInitSettings(context)
                    .withInitListener(new NameActivity())
                    .initialize();
        }
    }

    @Override
    public void onInitialized(AudienceNetworkAds.InitResult result) {
        Log.d(AudienceNetworkAds.TAG, result.getMessage());
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nameText = (EditText) findViewById(R.id.nameText);
        enterButton = (Button) findViewById(R.id.enterButton);
        nameText.getText( ).toString( );
        initialize(getApplicationContext());
        AudienceNetworkAds.initialize(this);
        adView = new AdView(this, "301912531524676_301914931524436", AdSize.BANNER_HEIGHT_90);
        //AdSettings.addTestDevice("8c15c671-3f3c-449a-85fb-ce33c6fbe5fa");
        // Find the Ad Container
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        // Request an ad
        adView.loadAd();
        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Toast.makeText(
                        NameActivity.this,
                        "Error: " + adError.getErrorMessage(),
                        Toast.LENGTH_LONG)
                        .show();
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        };

        // Request an ad
        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
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


    }

    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        super.onDestroy();
    }


}
