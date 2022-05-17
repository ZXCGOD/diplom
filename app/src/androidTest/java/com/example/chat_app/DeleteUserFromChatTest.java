package com.example.chat_app;

import static com.example.chat_app.Constants.SERVER_PATH;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;





public class DeleteUserFromChatTest {
    private WebSocket webSocket;


    @Before
    public void before(){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(SERVER_PATH).build();
        webSocket = client.newWebSocket(request, new SocketListener());

    }
    @Test
    public void deleteUserFromChat() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("purpose","deleteUserFromChat");
            jsonObject.put("id_chat","1");
            jsonObject.put("id_user","1");
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
        public void onMessage(WebSocket webSocket, String text) {
            super.onMessage(webSocket, text);


            boolean answerOfServer = true;
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