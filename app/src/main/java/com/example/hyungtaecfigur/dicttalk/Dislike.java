package com.example.hyungtaecfigur.dicttalk;

public class Dislike{

    private String id;
    private String comment_id;
    private String user_id;
    private String translation_id;
    private String sentence_id;
    private String reason_id;

    public Dislike(){}

    public Dislike(String id, String comment_id, String user_id, String translation_id, String sentence_id, String reason_id) {
        this.id = id;
        this.comment_id = comment_id;
        this.user_id = user_id;
        this.translation_id = translation_id;
        this.sentence_id = sentence_id;
        this.reason_id = reason_id;
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

    public String getReason_id() {
        return reason_id;
    }

    public void setReason_id(String reason_id) {
        this.reason_id = reason_id;
    }
}
