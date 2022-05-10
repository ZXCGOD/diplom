package com.example.chat_app;

import static com.example.chat_app.Constants.SERVER_PATH;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiateSocketConnection();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 10);



        findViewById(R.id.registerBtn)
                .setOnClickListener(v -> {

                    Intent intent = new Intent(this, RegistrationActivity.class);

                    startActivity(intent);

                });
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
                Toast.makeText(MainActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

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

                        User.init(jsonObject.getString("id"),jsonObject.getString("name"),jsonObject.getString("email"),jsonObject.getString("photo"));
                        Toast.makeText(MainActivity.this,
                                User.instance().getName(),
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MainActivity.this, ListOfChatsActivity.class);

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

    private void initializeView() {

        EditText editTextEmail = findViewById(R.id.editTextEmail);
        EditText editTextPassword = findViewById(R.id.editTextPassword);


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