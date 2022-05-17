package com.example.chat_app;

import static com.example.chat_app.Constants.SERVER_PATH;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class AddUserToChatTest {
    WebSocket webSocket;
    @Test
    public void addUserToChat() {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                super.onOpen(webSocket, response);
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("purpose", "addUserToChat");
                    jsonObject.put("email", "peregudow.2016@yandex.ru");
                    jsonObject.put("id_chat", "2");
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
        webSocket.close(1000,"Close via end of test");
    }

}
