package com.example.chat_app;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.WebSocket;

public class UserAdapter extends RecyclerView.Adapter{

    private LayoutInflater inflater;
    private List<JSONObject> users = new ArrayList<>();
    private Context parent;
    private WebSocket webSocket;
    public UserAdapter(LayoutInflater inflater, Context parent, WebSocket webSocket) {
        this.inflater = inflater;
        this.parent = parent;
        this.webSocket = webSocket;
    }

    private  class UserHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        TextView idTxt;
        TextView emailTxt;
        ImageView personImage;
        ImageView deleteUser;

        public UserHolder(@NonNull View itemView) {
            super(itemView);

            nameTxt = itemView.findViewById(R.id.nameTxt);
            emailTxt = itemView.findViewById(R.id.emailTxt);
            idTxt = itemView.findViewById(R.id.idTxt);
            personImage = itemView.findViewById(R.id.personImage);
            deleteUser = itemView.findViewById(R.id.deleteUser);
            deleteUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = getAdapterPosition();
                    new AlertDialog.Builder(parent)
                            .setTitle("Delete user")
                            .setMessage("Are you sure you want to delete this user from chat?")

                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    JSONObject user = users.get(index);
                                    JSONObject jsonObject = new JSONObject();

                                    try {
                                        jsonObject.put("purpose","deleteUserFromChat");
                                        jsonObject.put("id_chat",Chat.instance().getId());
                                        jsonObject.put("id_user",user.getString("id"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                        webSocket.send(jsonObject.toString());
                                    clear();
                                        jsonObject.remove("purpose");
                                    try {
                                        jsonObject.put("id",Chat.instance().getId());
                                        jsonObject.put("purpose","getListOfUsersInChat");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                        webSocket.send(jsonObject.toString());
                                }
                            })

                            .setNegativeButton("No", null)
                            .show();
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
            if(User.instance().getId().equals(user.getString("id"))){
                chatHolder.deleteUser.setVisibility(View.INVISIBLE);
            }
            byte[] bytes = Base64.decode(user.getString("image"), Base64.DEFAULT);
            Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            chatHolder.personImage.setImageBitmap(bm);
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

    public void clear() {
        int size = users.size();
        users.clear();
        notifyItemRangeRemoved(0, size);
    }



}