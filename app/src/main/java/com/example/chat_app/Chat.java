package com.example.chat_app;

public class Chat {

    private String id;
    private String name;
    private static Chat chat;

    public Chat(String id ,String name ){
        this.id = id;
        this.name = name;
    }
    public Chat(){
        this.id = "";
        this.name = "";

    }
    public static Chat instance(){
        if(chat == null){
            chat = new Chat();
        }
        return chat;
    }
    public static void init(String id, String name){

        chat = new Chat(id,name);

    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
