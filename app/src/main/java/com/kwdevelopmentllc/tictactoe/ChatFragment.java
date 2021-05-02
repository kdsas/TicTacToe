package com.kwdevelopmentllc.tictactoe;



import android.os.Bundle;


import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import org.json.JSONException;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import java.util.ArrayList;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.pm.PackageManager;



public  class ChatFragment extends Fragment implements TextWatcher{
    Button buttonSend, buttonGame;
    EditText editTextSend;
    ArrayList array_list;
    View view;
    String chatname;
    String chatString;
    WebView chatWebView;
    private WebSocket webSocket;
    private String SERVER_PATH = "ws://192.168.1.213:3000";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_chat, container, false);
        editTextSend = (EditText) view.findViewById(R.id.editTextSend);
        buttonSend = (Button) view.findViewById(R.id.buttonSend);
        chatWebView = (WebView) view.findViewById(R.id.chatWebView);
        array_list = new ArrayList( );
        chatname = PrefConfig.loadName(getActivity( ));
        buttonGame = (Button) view.findViewById(R.id.buttonGame);
        chatWebView.setWebViewClient(new WebViewClient());
        chatWebView.getSettings().setJavaScriptEnabled(true);
        chatWebView.getSettings().setDomStorageEnabled(true);
        chatWebView.setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        chatWebView.loadUrl("http://kwdevgames.com/websockets.html");
        initiateSocketConnection( );
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);

        buttonGame.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                getActivity( ).onBackPressed( );
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener( ) {
            @Override
            public void onClick(View v) {
                chatString = chatname + ":" + editTextSend.getText( ).toString( );

                webSocket.send(chatString);


                JSONObject jsonObject = new JSONObject();
                try {


                    jsonObject.put("message", chatString);//sends message to socket server
                    webSocket.send(jsonObject.toString());

                    jsonObject.put("isSent", true);

                    resetMessageEdit();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

        });


        return view;


    }


    private void resetMessageEdit() {

        editTextSend.removeTextChangedListener( this);
        editTextSend.setText("");
       editTextSend.addTextChangedListener(this);

    }
    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient( );
        Request request = new Request.Builder( ).url(SERVER_PATH).build( );
        webSocket = client.newWebSocket(request, new SocketListener( ));//starts socket up

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

    }





    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            getActivity( ).runOnUiThread(() -> {

                System.out.println("hello");

            });

        }

    }
}