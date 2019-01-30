package com.example.hyungtaecfigur.dicttalk;

public class Dictionary{

    private String id;
    private String firstLanguage;
    private String secondLanguage;

    public Dictionary() {
    }

    public Dictionary(String id, String firstLanguage, String secondLanguage) {
        this.id = id;
        this.firstLanguage = firstLanguage;
        this.secondLanguage = secondLanguage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }
}
