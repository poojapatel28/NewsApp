package com.pl.dell.news;

/**
 * Created by DELL on 29-05-2017.
 */
public class User {

String name;
    String email;
    String urltopic;

    public User()
    {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrltopic() {
        return urltopic;
    }

    public void setUrltopic(String password) {
        this.urltopic = password;
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.urltopic = password;
    }
}
