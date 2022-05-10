package com.example.chat_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter{

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JSONObject> chats = new ArrayList<>();
    private Context parent;

    public ChatAdapter(LayoutInflater inflater,Context parent) {
        this.inflater = inflater;
        this.parent = parent;
    }

    private  class ChatHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        TextView idTxt;

        public ChatHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            idTxt = itemView.findViewById(R.id.idTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionIndex = getAdapterPosition();
                    JSONObject chat = chats.get(positionIndex);
                    String id = null;
                    String name = null;
                    try {
                        id = chat.getString("id");
                        name = chat.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Chat.init(id,name);
                    Intent intent = new Intent(parent, ChatActivity.class);

                    ListOfChatsActivity.toChatActivity(parent);

                }
            });
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;


        itemView = inflater.inflate(R.layout.item_chat, parent, false);



                return new ChatAdapter.ChatHolder(itemView);


        }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        JSONObject chat = chats.get(position);
        try {

            ChatAdapter.ChatHolder chatHolder = (ChatAdapter.ChatHolder) holder;
            chatHolder.nameTxt.setText(chat.getString("name"));
            chatHolder.idTxt.setText(chat.getString("id"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void addItem (JSONObject jsonObject) {
        chats.add(jsonObject);
        notifyDataSetChanged();
    }



}
