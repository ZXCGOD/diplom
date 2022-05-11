package com.example.chat_app;

public class User {
    public String id;
    public String name;
    public String email;
    public String photo;
    public String password;
    private static User user;
    public  User(String id, String name, String email, String photo, String password){
        this.id = id;
        this.name = name;
        this.email = email;
        this.photo = photo;
        this.password = password;

    }
    public User(){
        this.id = "";
        this.name = "";
        this.email = "";
        this.photo = "";
        this.password = "";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static User instance(){
        if(user == null){
            user = new User();
        }
        return user;
    }
    public static void init(String id, String name, String email, String photo,String password){

            user = new User(id,name,email,photo,password);

    }

    public String getEmail() {
        return email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public static void setUser(User user) {
        User.user = user;
    }
}

