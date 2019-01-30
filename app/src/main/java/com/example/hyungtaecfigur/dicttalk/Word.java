package com.example.hyungtaecfigur.dicttalk;

import com.google.firebase.database.ServerValue;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Word{
    private String id;
    private String user_id;
    private String language_id;
    private String text;
    private long time;

    public Word(){}

    public Word(String id, String user_id, String language_id, String text, long time) {
        this.id = id;
        this.user_id = user_id;
        this.language_id = language_id;
        this.text = text;
        this.time = time;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Word{" +
                "id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", language_id='" + language_id + '\'' +
                ", text='" + text + '\'' +
                ", time=" + time +
                '}';
    }
}
