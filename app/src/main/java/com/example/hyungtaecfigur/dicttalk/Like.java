package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Like{
    private String id;
    private String comment_id;
    private String user_id;
    private String translation_id;
    private String sentence_id;

    public Like(){}

    public Like(String id, String comment_id, String user_id, String translation_id, String sentence_id) {
        this.id = id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.translation_id = translation_id;
        this.sentence_id = sentence_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getComment_id() {
        return comment_id;
    }

    public void setComment_id(String comment_id) {
        this.comment_id = comment_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTranslation_id() {
        return translation_id;
    }

    public void setTranslation_id(String translation_id) {
        this.translation_id = translation_id;
    }

    public String getSentence_id() {
        return sentence_id;
    }

    public void setSentence_id(String sentence_id) {
        this.sentence_id = sentence_id;
    }
}
