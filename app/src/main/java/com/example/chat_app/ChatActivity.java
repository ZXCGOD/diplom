package com.example.chat_app;


import static com.example.chat_app.Constants.SERVER_PATH;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatActivity extends AppCompatActivity implements TextWatcher {

    private WebSocket webSocket;
    private EditText messageEdit;
    private View sendBtn, pickImgBtn, backImgBtn, moreImgBtn;
    private RecyclerView recyclerView;
    private int IMAGE_REQUEST_ID = 1;
    private MessageAdapter messageAdapter;
    private ArrayList<User> userList;
    private TextView nameOfChatTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initiateSocketConnection();


    }

    @Override
    public void onResume() {
        super.onResume();
        webSocket.close(1000,"onResume");
        initiateSocketConnection();
    }


    @Override
    public void onBackPressed() {
        if (false) {
            super.onBackPressed();
        } else {
            Intent intent = new Intent(this, ListOfChatsActivity.class);
            startActivity(intent);
        }
    }

    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        String string = s.toString().trim();

        if (string.isEmpty()) {
            resetMessageEdit();
        } else {

            sendBtn.setVisibility(View.VISIBLE);
            pickImgBtn.setVisibility(View.INVISIBLE);
        }

    }

    private void resetMessageEdit() {

        messageEdit.removeTextChangedListener(this);

        messageEdit.setText("");
        sendBtn.setVisibility(View.INVISIBLE);
        pickImgBtn.setVisibility(View.VISIBLE);

        messageEdit.addTextChangedListener(this);

    }

    private class SocketListener extends WebSocketListener {




        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            messageAdapter.clear();
        }
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("purpose", "getListOfMessages");
                    jsonObject.put("id_user", User.instance().getId());
                    jsonObject.put("name_user",User.instance().getName());
                    jsonObject.put("id_chat", Chat.instance().getId());

                    webSocket.send(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Toast.makeText(ChatActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();

                initializeView();
            });

        }

        @SuppressLint("NewApi")
        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {


                try {
                    JSONObject jsonObject = new JSONObject(text);



                        if (jsonObject.getString("id_user").equals(User.instance().getId())) {

                            jsonObject.put("isSent", true);

                        } else {

                            jsonObject.put("isSent", false);

                        }

                        if (jsonObject.getString("id_chat").equals(Chat.instance().getId())) {



                            messageAdapter.addItem(jsonObject);

                        }
                        recyclerView.scrollToPosition(messageAdapter.getItemCount() - 1);

                    } catch(JSONException e){
                        e.printStackTrace();
                    }

            });

        }
    }

    private void initializeView() {

        messageEdit = findViewById(R.id.messageEdit);
        sendBtn = findViewById(R.id.sendBtn);
        pickImgBtn = findViewById(R.id.pickImgBtn);
        backImgBtn = findViewById(R.id.backImgBtn);
        recyclerView = findViewById(R.id.recyclerView);
        moreImgBtn = findViewById(R.id.moreImgBtn);
        nameOfChatTxt = findViewById(R.id.nameOfChatTxt);
        messageAdapter = new MessageAdapter(getLayoutInflater());
        recyclerView.setAdapter(messageAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(Chat.instance().getType()){
            moreImgBtn.setVisibility(View.VISIBLE);
        } else {
            moreImgBtn.setVisibility(View.INVISIBLE);
        }

        nameOfChatTxt.setText(Chat.instance().getName());
        messageEdit.addTextChangedListener(this);

        backImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ListOfChatsActivity.class);

            startActivity(intent);
         });

        moreImgBtn.setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatInfoActivity.class);

            startActivity(intent);
        });
        sendBtn.setOnClickListener(v -> {

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("purpose", "message");
                jsonObject.put("name_user", User.instance().getName());
                jsonObject.put("id_user", User.instance().getId());
                jsonObject.put("id_chat", Chat.instance().getId());
                jsonObject.put("message", messageEdit.getText().toString());

                webSocket.send(jsonObject.toString());

                jsonObject.put("isSent", true);
                messageAdapter.addItem(jsonObject);

                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

                resetMessageEdit();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_REQUEST_ID && resultCode == RESULT_OK) {

            try {
                InputStream is = getContentResolver().openInputStream(data.getData());
                Bitmap image = BitmapFactory.decodeStream(is);

                sendImage(image);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    private void sendImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);

        String base64String = Base64.encodeToString(outputStream.toByteArray(),
                Base64.DEFAULT);

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("name", User.instance().getName());
            jsonObject.put("image", base64String);

            webSocket.send(jsonObject.toString());

            jsonObject.put("isSent", true);

            messageAdapter.addItem(jsonObject);

            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getListOfMessages(){

    }


}