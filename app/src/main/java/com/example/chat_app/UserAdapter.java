package com.example.chat_app;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
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

public class UserAdapter extends RecyclerView.Adapter{

    private static final int TYPE_MESSAGE_SENT = 0;
    private static final int TYPE_MESSAGE_RECEIVED = 1;
    private static final int TYPE_IMAGE_SENT = 2;
    private static final int TYPE_IMAGE_RECEIVED = 3;

    private LayoutInflater inflater;
    private List<JSONObject> users = new ArrayList<>();
    private Context parent;
    private ChatInfoActivity chatActivity = new ChatInfoActivity();
    public UserAdapter(LayoutInflater inflater,Context parent) {
        this.inflater = inflater;
        this.parent = parent;
    }

    private  class UserHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        TextView idTxt;
        TextView emailTxt;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            idTxt = itemView.findViewById(R.id.idTxt);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int positionIndex = getAdapterPosition();
                    JSONObject user = users.get(positionIndex);
                    String id_user = null;
                    String id_chat = Chat.instance().getId();
                    try {
                        id_user = user.getString("id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("id_user :",id_user);
                    Log.i("id_chat :",id_chat);
                }
            });
        }
    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView;


        itemView = inflater.inflate(R.layout.item_user, parent, false);



        return new UserAdapter.UserHolder(itemView);


    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        JSONObject user = users.get(position);
        try {

            UserAdapter.UserHolder chatHolder = ( UserAdapter.UserHolder) holder;
            chatHolder.emailTxt.setText(user.getString("email"));
            chatHolder.nameTxt.setText(user.getString("name"));
            chatHolder.idTxt.setText(user.getString("id"));



        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public int getItemCount() {
        return users.size();
    }

    public void addItem (JSONObject jsonObject) {
        users.add(jsonObject);
        notifyDataSetChanged();
    }



}