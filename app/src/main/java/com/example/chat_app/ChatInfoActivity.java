package com.example.chat_app;


import static com.example.chat_app.Constants.SERVER_PATH;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ChatInfoActivity extends AppCompatActivity {

    private WebSocket webSocket;
    private UserAdapter userAdapter;
    private ImageView backToChatBtn;
    private TextView nameOfChatTxt;
    private ImageView addUserBtn;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_info);

        initiateSocketConnection();
    }

    @Override
    public void onResume() {
        super.onResume();
        webSocket.close(1000,"onResume");
        initiateSocketConnection();
    }



    private void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    private class SocketListener extends WebSocketListener {


        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            userAdapter.clear();
        }

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(ChatInfoActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("purpose", "getListOfUsersInChat");
                    jsonObject.put("id",Chat.instance().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webSocket.send(jsonObject.toString());
                initializeView();
            });

        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);

                    userAdapter.addItem(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void initializeView() {


        addUserBtn = findViewById(R.id.addUserBtn);
        backToChatBtn = findViewById(R.id.backToChatBtn);

        recyclerView = findViewById(R.id.recyclerViewOfChats);
        nameOfChatTxt = findViewById(R.id.nameOfChatTxt);
        nameOfChatTxt.setText(Chat.instance().getName());

        userAdapter = new UserAdapter( getLayoutInflater(),ChatInfoActivity.this,webSocket);
        recyclerView.setAdapter(userAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        findViewById(R.id.addUserBtn).setOnClickListener(v -> {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            final EditText edittext = new EditText(this);
            edittext.setHint("Enter email of user");
            alert.setTitle("Add user");

            alert.setView(edittext);

            alert.setPositiveButton("Add user", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("purpose","addUserToChat");
                        jsonObject.put("email", edittext.getText().toString());
                        jsonObject.put("id_chat",Chat.instance().getId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                  webSocket.send(jsonObject.toString());
                    webSocket.close(1000,"onResume");
                    initiateSocketConnection();
                }
            });
            alert.setCancelable(true);
            alert.show();
        });

        findViewById(R.id.backToChatBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, ChatActivity.class);

            startActivity(intent);
        });

    }





}