package com.example.chat_app;

import static com.example.chat_app.Constants.SERVER_PATH;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class ListOfChatsActivity extends AppCompatActivity {



    private WebSocket webSocket;
    private ChatAdapter chatAdapter;
    private ImageView profileImgBtn;
    private Spinner spinner;
    private RecyclerView recyclerView;

    public static void toChatActivity(Context context) {
        Intent intent = new Intent(context, ChatActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_chats);

        initiateSocketConnection();
    }

    @Override
    public void onResume() {
        super.onResume();
        webSocket.close(1000,"onResume");
        initiateSocketConnection();
    }



    public void initiateSocketConnection() {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);

            runOnUiThread(() -> {
                Toast.makeText(ListOfChatsActivity.this,
                        "Socket Connection Successful!",
                        Toast.LENGTH_SHORT).show();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("purpose", "getListOfChats");
                    jsonObject.put("id",User.instance().getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webSocket.send(jsonObject.toString());

                initializeView();
            });

        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            super.onClosed(webSocket, code, reason);
            chatAdapter.clear();
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);

            runOnUiThread(() -> {

                try {
                    JSONObject jsonObject = new JSONObject(text);

                    chatAdapter.addItem(jsonObject);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            });

        }
    }

    private void initializeView() {


        profileImgBtn = findViewById(R.id.profileImgBtn);
        spinner = findViewById(R.id.spinner);

        recyclerView = findViewById(R.id.recyclerViewOfChats);




        chatAdapter = new ChatAdapter( getLayoutInflater(),this);

        recyclerView.setAdapter(chatAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.profileImgBtn).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);

            startActivity(intent);
        });




        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView adapter, View v, int i, long lng) {
                if(adapter.getItemAtPosition(i).toString().equals("Create chat")){
                    AlertDialog.Builder alert = new AlertDialog.Builder(ListOfChatsActivity.this);
                    final EditText edittext = new EditText(ListOfChatsActivity.this);
                    edittext.setHint("Enter email of user");
                    alert.setTitle("Create chat with user");
                    alert.setView(edittext);
                    alert.setPositiveButton("Create chat", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("purpose","createChat");
                                jsonObject.put("email", edittext.getText().toString());
                                jsonObject.put("id_user",User.instance().getId());
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
                }   else {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ListOfChatsActivity.this);
                    final EditText edittext = new EditText(ListOfChatsActivity.this);
                    edittext.setHint("Enter name of group chat");
                    alert.setTitle("Create group chat");
                    alert.setView(edittext);
                    alert.setPositiveButton("Create group chat", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            JSONObject jsonObject = new JSONObject();
                            try {
                                jsonObject.put("purpose","createGroupChat");
                                jsonObject.put("name", edittext.getText().toString());
                                jsonObject.put("id_user",User.instance().getId());
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
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }



}