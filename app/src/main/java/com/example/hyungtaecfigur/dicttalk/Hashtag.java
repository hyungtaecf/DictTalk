package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Hashtag {
    private String id;
    private String dictionary_id;
    private String name;
    private String description;

    public Hashtag(){}

    public Hashtag(String id, String dictionary_id, String name, String description) {
        this.id = id;
        this.dictionary_id = dictionary_id;
        this.name = name;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDictionary_id() {
        return dictionary_id;
    }

    public void setDictionary_id(String dictionary_id) {
        this.dictionary_id = dictionary_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}