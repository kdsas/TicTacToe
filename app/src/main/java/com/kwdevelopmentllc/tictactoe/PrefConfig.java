package com.kwdevelopmentllc.tictactoe;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefConfig {

    private static final String MY_PREFERENCE_NAME ="com.kwdevelopmentllc.tictactoe";
    private static final String chatName ="chatName" ;

    public static void saveName(Context context, String name){
        SharedPreferences pref= context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=pref.edit();
        editor.putString(chatName, name);
        editor.apply();

    }

    public static String loadName(Context context){

        SharedPreferences pref= context.getSharedPreferences(MY_PREFERENCE_NAME, Context.MODE_PRIVATE);
        return pref.getString(chatName, "");
    }
}
