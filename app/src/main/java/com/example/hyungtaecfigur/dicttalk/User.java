package com.example.hyungtaecfigur.dicttalk;

public class User {
    private String id;
    private String nickname;
    private String bio; //EDITABLE
    private int verified; //EDITABLE
    private String password; //EDITABLE
    private String email;
    private String image; //EDITABLE
    private int reputation; //EDITABLE
    private int likes; //EDITABLE
    private int dislikes; //EDITABLE
    private int comments; //EDITABLE
    private int translations; //EDITABLE
    private int sentences; //EDITABLE
    private int synonym; //EDITABLE
    private int antonym; //EDITABLE
    private int meaning; //EDITABLE
    private int words; //EDITABLE

    public User() {
    }

    public User(String id, String nickname, String bio, int verified, String password, String email,
                String image, int reputation, int likes, int dislikes, int comments, int translations,
                int sentences, int synonym, int antonym, int meaning, int words) {
        this.id = id;
        this.nickname = nickname;
        this.bio = bio;
        this.verified = verified;
        this.password = password;
        this.email = email;
        this.image = image;
        this.reputation = reputation;
        this.likes = likes;
        this.dislikes = dislikes;
        this.comments = comments;
        this.translations = translations;
        this.sentences = sentences;
        this.synonym = synonym;
        this.antonym = antonym;
        this.meaning = meaning;
        this.words = words;
    }

    public User(String id, String nickname, String password, String email) {
        this.id = id;
        this.nickname = nickname;
        bio = "";
        this.password = password;
        this.email = email;
        dislikes = 0;
        comments = 0;
        translations = 0;
        sentences = 0;
        synonym = 0;
        antonym = 0;
        meaning = 0;
        words = 0;
        image = "https://firebasestorage.googleapis.com/v0/b/dict-talk-sunmoon.appspot.com/o/default-profile.png?alt=media&token=f9900de2-0cac-48cc-96e3-189de3c9d654";
        reputation = 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public int getVerified() {
        return verified;
    }

    public void setVerified(int verified) {
        this.verified = verified;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getReputation() {
        return reputation;
    }

    public void setReputation(int reputation) {
        this.reputation = reputation;
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

    public int getTranslations() {
        return translations;
    }

    public void setTranslations(int translations) {
        this.translations = translations;
    }

    public int getSentences() {
        return sentences;
    }

    public void setSentences(int sentences) {
        this.sentences = sentences;
    }

    public int getSynonym() {
        return synonym;
    }

    public void setSynonym(int synonym) {
        this.synonym = synonym;
    }

    public int getAntonym() {
        return antonym;
    }

    public void setAntonym(int antonym) {
        this.antonym = antonym;
    }

    public int getMeaning() {
        return meaning;
    }

    public void setMeaning(int meaning) {
        this.meaning = meaning;
    }

    public int getWords() {
        return words;
    }

    public void setWords(int words) {
        this.words = words;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", bio='" + bio + '\'' +
                ", verified=" + verified +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", image='" + image + '\'' +
                ", reputation=" + reputation +
                ", likes=" + likes +
                ", dislikes=" + dislikes +
                ", comments=" + comments +
                ", translations=" + translations +
                ", sentences=" + sentences +
                ", synonym=" + synonym +
                ", antonym=" + antonym +
                ", meaning=" + meaning +
                ", words=" + words +
                '}';
    }
}