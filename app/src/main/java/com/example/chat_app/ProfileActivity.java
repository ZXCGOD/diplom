package com.example.chat_app;

import static com.example.chat_app.Constants.SERVER_PATH;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
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

public class ProfileActivity extends AppCompatActivity {

    private WebSocket webSocket;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);



        findViewById(R.id.backToMain)
                .setOnClickListener(v -> {

                    Intent intent = new Intent(this, ListOfChatsActivity.class);
                    startActivity(intent);

                });
        initiateSocketConnection();

    }

    @Override
    public void onResume() {
        super.onResume();
        webSocket.close(1000,"norm");
        initiateSocketConnection();

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
                Toast.makeText(ProfileActivity.this,
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
                    jsonObject.put("isSent", false);



                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }




        private void initializeView() {

            EditText editTextEmail = findViewById(R.id.editTextEmail);
            EditText editTextName = findViewById(R.id.editTextName);
            EditText editTextPassword = findViewById(R.id.editTextPassword);
            EditText editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);

            editTextEmail.setText(User.instance().getEmail());
            editTextName.setText(User.instance().getName());


            findViewById(R.id.btnEdit).setOnClickListener(v -> {

                JSONObject jsonObject = new JSONObject();



                try {
                    jsonObject.put("purpose", "editProfile");
                    jsonObject.put("id", User.instance().getId());
                    if(editTextPassword.getText().length() >= 9 ){
                        jsonObject.put("password", editTextPassword.getText().toString());
                    }   else if(editTextPassword.getText().length()  >=1) {
                        Toast.makeText(ProfileActivity.this,"Пароль должен содержать более 9 символов", Toast.LENGTH_SHORT).show();
                    } else {
                        jsonObject.put("password", User.instance().getPassword());
                    }
                    if(editTextEmail.getText().equals(User.instance().getEmail())) {
                        jsonObject.put("email", User.instance().getEmail());
                    } else {
                        jsonObject.put("email", editTextEmail.getText().toString());
                    }
                    if(editTextName.equals(User.instance().getName())) {
                        jsonObject.put("name", User.instance().getName());
                    }   else {
                        jsonObject.put("name", editTextName.getText().toString());
                    }
                    if(!editTextPassword.getText().toString().equals(editTextRepeatPassword.getText().toString())) {
                        Toast.makeText(ProfileActivity.this,
                                "Пароли не совпадают",
                                Toast.LENGTH_SHORT).show();
                    }else {

                        webSocket.send(jsonObject.toString());
                        User.init(User.instance().getId(),jsonObject.getString("name"),jsonObject.getString("email"),"dont forget photo!!!!", jsonObject.getString("password") );
                        Toast.makeText(ProfileActivity.this," " + User.instance().getName() + " ", Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }

            );




        }

    }
}