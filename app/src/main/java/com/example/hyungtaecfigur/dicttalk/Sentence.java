package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Sentence{
    private String id;
    private String meaning_id;
    private String user_id;
    private String body;
    private int time;

    public Sentence(){}

    public Sentence(String id, String meaning_id, String user_id, String body, int time) {
        this.id = id;
        this.meaning_id = meaning_id;
        this.user_id = user_id;
        this.body = body;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMeaning_id() {
        return meaning_id;
    }

    public void setMeaning_id(String meaning_id) {
        this.meaning_id = meaning_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
