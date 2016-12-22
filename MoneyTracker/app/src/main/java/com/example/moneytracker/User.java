package com.example.moneytracker;

/**
 * Created by mohit on 22/5/16.
 */
public class User {
    private String name;
    private String number;
    private String password;

    User(String name,String number,String password){
        this.name = name;
        this.number = number;
        this.password = password;
    }

    public String getName(){
        return this.name;
    }
    public String getPassword(){
        return this.password;
    }
    public String getNumber(){
        return this.number;
    }
}
