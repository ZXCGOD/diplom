package com.example.chat_app;


import static com.example.chat_app.Constants.SERVER_PATH;

import org.json.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.util.concurrent.FutureTask;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

import okhttp3.WebSocketListener;


public class RegistrationTest{
    private WebSocket webSockets;

    @Test
    public void registration() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSockets = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("purpose", "registration");
                    jsonObject.put("email", "peregudow.2016@yandex.ru");
                    jsonObject.put("name", "Vladislav Maksimovich");
                    jsonObject.put("password", "MarinaSergeevnaLuchshaya");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                webSocket.send(jsonObject.toString());
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                super.onMessage(webSocket, text);
                boolean answerOfServer = false;
                try {
                    JSONObject jsonObject = new JSONObject(text);
                    answerOfServer = jsonObject.getBoolean("ok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Assert.assertTrue(answerOfServer);
            }
        });
    }
    @After
    public void after(){
        webSockets.close(1000,"Close via end of test");
    }
}