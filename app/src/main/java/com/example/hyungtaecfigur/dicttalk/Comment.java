package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Comment {
    private String id;
    private String meaning_id;
    private String user_id;
    private String translation_id;
    private String sentence_id;
    private String referencedComment_id;
    private String body;
    private Date time;

    public Comment() {
    }

    public Comment(String id, String meaning_id, String user_id, String translation_id, String sentence_id, String referencedComment_id, String body, Date time) {
        this.id = id;
        this.meaning_id = meaning_id;
        this.user_id = user_id;
        this.translation_id = translation_id;
        this.sentence_id = sentence_id;
        this.referencedComment_id = referencedComment_id;
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

    public String getReferencedComment_id() {
        return referencedComment_id;
    }

    public void setReferencedComment_id(String referencedComment_id) {
        this.referencedComment_id = referencedComment_id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
