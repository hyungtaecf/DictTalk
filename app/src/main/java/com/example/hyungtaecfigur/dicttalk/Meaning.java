package com.example.hyungtaecfigur.dicttalk;

public class Meaning{

    private String id;
    private String word_id;
    private String dictionary_id;
    private String user_id;
    private String text;
    private long time;

    public Meaning(){}

    public Meaning(String id, String word_id, String dictionary_id, String user_id, String text, long time) {
        this.id = id;
        this.word_id = word_id;
        this.dictionary_id = dictionary_id;
        this.user_id = user_id;
        this.text = text;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord_id() {
        return word_id;
    }

    public void setWord_id(String word_id) {
        this.word_id = word_id;
    }

    public String getDictionary_id() {
        return dictionary_id;
    }

    public void setDictionary_id(String dictionary_id) {
        this.dictionary_id = dictionary_id;
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
}
