package com.example.hyungtaecfigur.dicttalk;

public class MeaningItem {
    private String language;
    private String word;
    private String meaning;
    private String meaning_id;
    private String word_id;
    private String dictionary_id;

    public MeaningItem() {
    }

    public MeaningItem(String language, String word, String meaning, String meaning_id, String word_id, String dictionary_id) {
        this.language = language;
        this.word = word;
        this.meaning = meaning;
        this.meaning_id = meaning_id;
        this.word_id = word_id;
        this.dictionary_id = dictionary_id;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public String getMeaning_id() {
        return meaning_id;
    }

    public void setMeaning_id(String meaning_id) {
        this.meaning_id = meaning_id;
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
}
