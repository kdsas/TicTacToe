package com.kwdevelopmentllc.tictactoe;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import androidx.annotation.Nullable;
public class BackgroundMusic extends Service {
    MediaPlayer mediaPlayer;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.and_just_like_that);
        mediaPlayer.setLooping(true); // Set looping
        mediaPlayer.setVolume(25, 25);
    }
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        //Toast.makeText(getApplicationContext(), "Playing music in the background",    Toast.LENGTH_SHORT).show();
        return startId;
    }
    public void onStart(Intent intent, int startId) {
    }
    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        mediaPlayer.release();
    }
    @Override
    public void onLowMemory() {
    }
}