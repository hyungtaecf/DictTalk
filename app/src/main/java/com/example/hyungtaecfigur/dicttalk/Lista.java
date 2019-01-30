package com.example.hyungtaecfigur.dicttalk;

import java.util.Date;

public class Lista{
    private String id;
    private String user_id;
    private String name;
    private String type;

    public Lista(){}

    public Lista(String id, String user_id, String name, String type) {
        this.id = id;
        this.user_id = user_id;
        this.name = name;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
