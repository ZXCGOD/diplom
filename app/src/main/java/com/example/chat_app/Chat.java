package com.example.chat_app;

public class Chat {

    private String id;
    private String name;
    private boolean type;
    private String image;
    private static Chat chat;
    private boolean TYPE_CHAT = false;
    private boolean TYPE_GROUP_CHAT = true;
    public Chat(String id ,String name , boolean type, String image){
        this.id = id;
        this.name = name;
        this.type = type;
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public Chat(){
        this.id = "";
        this.name = "";
        this.type = false;
        this.image = "";
    }
    public static Chat instance(){
        if(chat == null){
            chat = new Chat();
        }
        return chat;
    }
    public static void init(String id, String name,boolean type,String image){

        chat = new Chat(id,name,type,image);

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
