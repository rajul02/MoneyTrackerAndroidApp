package com.example.moneytracker;

/**
 * Created by mohit on 23/5/16.
 */
public class UserTransaction {
    private String name;
    private String number;
    private int amount;

    UserTransaction(String name,String phone,int amount)
    {
        this.name=name;
        this.amount=amount;
        this.number=phone;
    }

    public String getName() {
        return this.name;
    }
    public String getNumber() {
        return this.number;
    }
    public int getAmount(){
        return this.amount;
    }
}

