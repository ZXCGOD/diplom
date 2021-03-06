package com.example.chat_app;

import static com.example.chat_app.Constants.APP_PREFERENCES;
import static com.example.chat_app.Constants.SERVER_PATH;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ProfileActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private EditText editTextEmail;
    private EditText editTextName;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private ImageView profileImg,logout;
    private SharedPreferences mSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initiateSocketConnection();
    }

    @Override
    public void onBackPressed() {
        if (false) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this, ListOfChatsActivity.class);
            webSocket.close(1000,"");
            startActivity(intent);
        }
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
                getUserPhoto();
                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {

                    JSONObject jsonObject = new JSONObject(text);
                    byte[] bytes = Base64.decode(jsonObject.getString("image"), Base64.DEFAULT);
                    Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                    profileImg.setImageBitmap(bm);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }




        private void initializeView() {

            editTextEmail = findViewById(R.id.editTextEmail);
            editTextName = findViewById(R.id.editTextName);
            editTextPassword = findViewById(R.id.editTextPassword);
            editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
            profileImg = findViewById(R.id.profileImg);
            editTextEmail.setText(User.instance().getEmail());
            editTextName.setText(User.instance().getName());
            logout = findViewById(R.id.logout);
            mSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = mSettings.edit();





            findViewById(R.id.backToMain)
                    .setOnClickListener(v -> {
                        Intent intent = new Intent(ProfileActivity.this,
                                ListOfChatsActivity.class);
                        webSocket.close(1000,"");
                        startActivity(intent);
                    });

            profileImg.setOnClickListener(v ->{
                Intent data = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                data.setType("image/*");
                data = Intent.createChooser(data,"???????????????? ?????????? ???????????????????? ??????????????");
                sActivityResultLauncher.launch(data);
            });

            logout.setOnClickListener(v ->{
                editor.clear().commit();
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                webSocket.close(1000,"");
                startActivity(intent);
            });

            findViewById(R.id.btnEdit).setOnClickListener(v -> {

                JSONObject jsonObject = new JSONObject();

                try {
                    jsonObject.put("purpose", "editProfile");
                    jsonObject.put("id", User.instance().getId());
                    if(editTextPassword.getText().length() >= 9 ){
                        jsonObject.put("password", editTextPassword.getText().toString());
                    }   else if(editTextPassword.getText().length()  >=1) {
                        Toast.makeText(ProfileActivity.this,"???????????? ???????????? ?????????????????? ?????????? 9 ????????????????",
                                Toast.LENGTH_SHORT).show();
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
                                "???????????? ???? ??????????????????",
                                Toast.LENGTH_SHORT).show();
                    }else {
                        webSocket.send(jsonObject.toString());
                        User.instance().setName(jsonObject.getString("name"));
                        User.instance().setEmail(jsonObject.getString("email"));

                                User.instance().setPassword(jsonObject.getString("password") );
                        Toast.makeText(ProfileActivity.this," " + User.instance().getName() + " ",
                                Toast.LENGTH_SHORT).show();
                    }



                } catch (JSONException e) {
                    e.printStackTrace();
                }
                }

            );




        }




    }


    public void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 1, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.NO_WRAP);

        JSONObject jsonObject = new JSONObject();
        if(base64String.equals("undefined")){
        }else {
            try {
                jsonObject.put("purpose", "changeProfileImage");
                jsonObject.put("id", User.instance().getId());
                jsonObject.put("image", base64String);
                webSocket.send(jsonObject.toString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void getUserPhoto() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("purpose", "getUserPhoto");
            jsonObject.put("id", User.instance().getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonObject.toString());
    }

    ActivityResultLauncher<Intent> sActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == RESULT_OK) {

                        Intent data = result.getData();
                        try {
                            InputStream is = getContentResolver().openInputStream(data.getData());
                            Bitmap image = BitmapFactory.decodeStream(is);
                            profileImg.setImageBitmap(image);
                            sendImage(image);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }

);

}