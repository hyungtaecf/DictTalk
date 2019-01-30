package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Translation{
    private String id;
    private String meaning_id;
    private String language_id;
    private String user_id;
    private String text;
    private long time;
    private String source;
    private String verified;
    private int likes;
    private int dislikes;
    private int comments;

    public Translation() {
    }

    public Translation(String id, String meaning_id, String language_id, String user_id, String text, long time, String source) {
        this.id = id;
        this.meaning_id = meaning_id;
        this.language_id = language_id;
        this.user_id = user_id;
        this.text = text;
        this.time = time;
        this.source = source;
        this.verified = "false";
        this.likes = 0;
        this.dislikes = 0;
        this.comments = 0;
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

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }

    public String getLanguage_id() {
        return language_id;
    }

    public void setLanguage_id(String language_id) {
        this.language_id = language_id;
    }
}
