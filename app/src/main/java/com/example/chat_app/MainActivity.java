package com.example.chat_app;

import static com.example.chat_app.Constants.APP_PREFERENCES;
import static com.example.chat_app.Constants.APP_PREFERENCES_EMAIL;
import static com.example.chat_app.Constants.APP_PREFERENCES_ID;
import static com.example.chat_app.Constants.APP_PREFERENCES_NAME;
import static com.example.chat_app.Constants.APP_PREFERENCES_PASSWORD;
import static com.example.chat_app.Constants.APP_PREFERENCES_PHOTO;
import static com.example.chat_app.Constants.SERVER_PATH;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MainActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private SharedPreferences mSettings;



    @Override
    protected void onCreate(Bundle savedInstanceState) {




        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        if(mSettings.contains(APP_PREFERENCES_ID)){
            User.init((mSettings.getString(APP_PREFERENCES_ID, "")),(mSettings.getString(APP_PREFERENCES_NAME, "")),
                    (mSettings.getString(APP_PREFERENCES_EMAIL, "")),(mSettings.getString(APP_PREFERENCES_PHOTO, "")),
                    (mSettings.getString(APP_PREFERENCES_PASSWORD, "")));
            Intent intent = new Intent(this, ListOfChatsActivity.class);
            startActivity(intent);
        }
        initiateSocketConnection();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);




    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }






    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                initializeView();
            });

        }
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);
                    if(jsonObject.getBoolean("ok")){

                        User.init(jsonObject.getString("id"),jsonObject.getString("name"),
                                jsonObject.getString("email"),jsonObject.getString("photo"),
                                jsonObject.getString("password"));

                        SharedPreferences.Editor editor = mSettings.edit();
                        editor.putString(APP_PREFERENCES_ID, jsonObject.getString("id"));
                        editor.putString(APP_PREFERENCES_EMAIL, jsonObject.getString("email"));
                        editor.putString(APP_PREFERENCES_PHOTO, jsonObject.getString("photo"));
                        editor.putString(APP_PREFERENCES_NAME, jsonObject.getString("name"));
                        editor.putString(APP_PREFERENCES_PASSWORD, jsonObject.getString("password"));
                        editor.apply();

                        Intent intent = new Intent(MainActivity.this, ListOfChatsActivity.class);
                        webSocket.close(1000,"");
                        startActivity(intent);
                    }   else    {
                        Toast.makeText(MainActivity.this,
                                "Неправильный логин или пароль",
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        webSocket.close(1000,"norm");
        initiateSocketConnection();
    }



    private void initializeView() {

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);


        findViewById(R.id.registerBtn)
                .setOnClickListener(v -> {
                    Intent intent = new Intent(this, RegistrationActivity.class);
                    startActivity(intent);
                });

        findViewById(R.id.enterBtn).setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("purpose", "authentication");
                jsonObject.put("email", editTextEmail.getText().toString());
                jsonObject.put("password", editTextPassword.getText().toString());

                    webSocket.send(jsonObject.toString());



            } catch (JSONException e) {
                e.printStackTrace();
            }

        });




    }
}