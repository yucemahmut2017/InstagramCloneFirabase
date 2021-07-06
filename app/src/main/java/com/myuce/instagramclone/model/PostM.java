package com.myuce.instagramclone.model;

public class PostM {

    String email;
    String comment;
    String downloandUrl;


    //creat constructor
    public  PostM(String email,String comment,String downloandUrl){

        this.email=email;
        this.comment=comment;
        this.downloandUrl=downloandUrl;


    }

    public String getComment() {
        return comment;
    }

    public String getDownloandUrl() {
        return downloandUrl;
    }

    public String getEmail() {
        return email;
    }
}
