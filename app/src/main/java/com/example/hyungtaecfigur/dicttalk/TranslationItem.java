package com.example.hyungtaecfigur.dicttalk;

public class TranslationItem {
    //Translation atributes
    private String translationId;
    private String translationText;
    private long time;
    private String source;
    private String verified;
    private int likes;
    private int dislikes;
    private int comments;

    //User atributes
    private String userId;
    private String nickname;
    private int reputation;
    private String image;

    //Others Ids
    private String wordId;
    private String meaningId;
    private String languageId; //translation's language

    private String wordText;

    public TranslationItem() {
    }

    public TranslationItem(String translationId, String translationText, long time, String source, String verified, int likes, int dislikes, int comments, String userId, String nickname, int reputation, String image, String wordId, String meaningId, String languageId, String wordText) {
        this.translationId = translationId;
        this.translationText = translationText;
        this.time = time;
        this.source = source;
        this.verified = verified;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.userId = userId;
        this.nickname = nickname;
        this.reputation = reputation;
        this.image = image;
        this.wordId = wordId;
        this.meaningId = meaningId;
        this.languageId = languageId;
        this.wordText = wordText;
    }

    public String getTranslationId() {
        return translationId;
    }

    public void setTranslationId(String translationId) {
        this.translationId = translationId;
    }

    public String getTranslationText() {
        return translationText;
    }

    public void setTranslationText(String translationText) {
        this.translationText = translationText;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getWordId() {
        return wordId;
    }

    public void setWordId(String wordId) {
        this.wordId = wordId;
    }

    public String getMeaningId() {
        return meaningId;
    }

    public void setMeaningId(String meaningId) {
        this.meaningId = meaningId;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getWordText() {
        return wordText;
    }

    public void setWordText(String wordText) {
        this.wordText = wordText;
    }
}
