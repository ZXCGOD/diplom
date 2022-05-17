package com.example.chat_app;



import org.json.*;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;

import okhttp3.WebSocketListener;


public class ExampleUnitTest {
    private WebSocket webSocket;
    String SERVER_PATH = "ws://192.168.1.4:3000";

    @Before
    public void before(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }
    @Test
    public void authentication() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("purpose", "authentication");
            jsonObject.put("email", "123");
            jsonObject.put("password", "123");
             } catch (JSONException e) {
            e.printStackTrace();
        }
        webSocket.send(jsonObject.toString());
    }

    @After
    public void after(){
        webSocket.close(1000,"Close via end of test");
    }

    private class SocketListener extends WebSocketListener {

        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            super.onOpen(webSocket, response);



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
            Assert.assertEquals(true,answerOfServer);



        }
    }

}