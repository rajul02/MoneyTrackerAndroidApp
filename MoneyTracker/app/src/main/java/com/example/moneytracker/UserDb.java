package com.example.moneytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.design.widget.TabLayout;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mohit on 23/5/16.
 */
public class UserDb extends SQLiteOpenHelper {

    public static final String Database_Name = "User.db";
    public static final String Table_Name_User= "Person";
    public static final String Col_1_Name = "pname";
    public static final String Col_2_Number = "pnumber";
    public static final String Col_3_Password = "ppassword";
    public static final String Col_4_log_status = "Logstatus";

    public static final String Table_Name_UserTransaction = "UserTransactions";
    public static final String Col_1_UTName = "utname";
    public static final String Col_2_UTNumber = "utnumber";
    public static final String Col_3_UTAmount = "utamount";
    public static final String Col_4_UTFromNumber = "utfromnumber";

    public static final String Create_Table_User = "Create Table If Not Exists " + Table_Name_User +
            "(" + Col_1_Name + " text," + Col_2_Number + " text primary key," + Col_3_Password + " text," +
            Col_4_log_status + " number)";

    public static final String Create_Table_UT = "Create Table If Not Exists " + Table_Name_UserTransaction + "(" +
            Col_1_UTName + " text," + Col_2_UTNumber + " text," + Col_3_UTAmount + " number," +
            Col_4_UTFromNumber + " number)";

    String sql;


    public UserDb(Context context) {
        super(context, Database_Name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Create_Table_User);
        db.execSQL(Create_Table_UT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        sql = "Drop Table If Exists "+ Table_Name_User;
        db.execSQL(sql);
        sql = "drop Table if exists " + Table_Name_UserTransaction;
        db.execSQL(sql);
        onCreate(db);
    }

    public void addUser(User user) {
        ContentValues values = new ContentValues();
        values.put(Col_1_Name,user.getName());
        values.put(Col_2_Number,user.getNumber());
        values.put(Col_3_Password,user.getPassword());
        values.put(Col_4_log_status,0);

        SQLiteDatabase db = this.getWritableDatabase();
        int id = (int) db.insertOrThrow(Table_Name_User,null,values);

        Log.d("mt","DataBase Insert Status: " + id);
        db.close();
    }


    public void seeAlldata(){
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "Select * from " + Table_Name_User;
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do {
                Log.d("mt","Data >> name: " +cursor.getString(0) + " number: " + cursor.getString(1) + " password: " + cursor.getString(2) + " Status: " + cursor.getInt(3));
            }while (cursor.moveToNext());
        }
        else {
            Log.d("mt","No Data");
        }

        cursor.close();
        db.close();
    }

    public User getUser(String number) {
        User user;

        SQLiteDatabase db = getReadableDatabase();

        sql = "Select * from "+ Table_Name_User+ " where "+ Col_2_Number +" = " + number;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            Log.d("mt",user.getName() + " " + user.getNumber() + " " + (user != null));
        }
        else {
            Log.d("mt","still data not found");
            return null;
        }

        cursor.close();
        db.close();

        return user;
    }

    public void clearall(){
        SQLiteDatabase db = this.getWritableDatabase();
        String sql = "Delete from " + Table_Name_User;
        //db.execSQL(sql);
        sql = "Delete from " + Table_Name_UserTransaction;
        //db.execSQL(sql);
        db.close();
    }

    public User getLoginUser(){
        User user;
        SQLiteDatabase db = this.getReadableDatabase();
        sql = "select * from " + Table_Name_User+" where " + Col_4_log_status + " = 1";
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            Log.d("mt","Login User Found");
            cursor.close();
            db.close();

            return user;
        }
        else{
            Log.d("mt","Login failed");
            cursor.close();
            db.close();
            return null;
        }
    }

    public boolean login(String usernumber,String userpass){
        User user = getUser(usernumber);
        Log.d("mt","Login Process start: ");
        if(user != null){
            if(user.getPassword().equalsIgnoreCase(userpass)){
                SQLiteDatabase db = this.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put(Col_4_log_status,1);
                int id  = db.update(Table_Name_User,values,Col_2_Number + " = " + usernumber,null);
                Log.d("mt","Update id: " + id);

                db.close();

                return true;
            }
            return false;
        }
        return false;

    }

    public void altertable(){
        SQLiteDatabase db = getWritableDatabase();
        onUpgrade(db,1,1);
        db.close();
    }

    public void logout(User user){
        if(user != null) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Col_4_log_status, 0);
            int id = db.update(Table_Name_User, values, Col_2_Number + " = " + user.getNumber(), null);
            Log.d("mt", "logout dbid: " + id);
            db.close();
        }

    }

    public void addUserTransaction(UserTransaction userTransaction){

        User user = getLoginUser();

        User newuser = getUserTransaction(userTransaction.getNumber(),user.getNumber());
        if(newuser != null){

            if(userTransaction.getAmount() == 0){
             SQLiteDatabase db = this.getWritableDatabase();
                int id = db.delete(Table_Name_UserTransaction,Col_2_UTNumber + " = " + newuser.getNumber() + " and " + Col_4_UTFromNumber + " = " + user.getNumber()  ,null);
                Log.d("mt","usertransaction deleted: " + id);

                db.close();
            }
            else {

                ContentValues values = new ContentValues();
                values.put(Col_3_UTAmount, userTransaction.getAmount());

                SQLiteDatabase db = this.getWritableDatabase();
                int id = db.update(Table_Name_UserTransaction, values, Col_2_UTNumber + " = " + newuser.getNumber(), null);
                Log.d("mt", "previous data updated");

                db.close();
            }
        }
        else {

            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Col_4_UTFromNumber, user.getNumber());
            values.put(Col_1_UTName, userTransaction.getName());
            values.put(Col_2_UTNumber, userTransaction.getNumber());
            values.put(Col_3_UTAmount, userTransaction.getAmount());

            int id = (int) db.insert(Table_Name_UserTransaction, null, values);
            Log.d("mt", "New Transaction Added: " + id);

            db.close();
        }
    }

    public List<UserTransaction> getAllTransaction(){
        List<UserTransaction> list = new ArrayList();

        User user = getLoginUser();

        SQLiteDatabase db = getReadableDatabase();

        sql = "select * from " + Table_Name_UserTransaction + " where " + Col_4_UTFromNumber + " = " + user.getNumber();
        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            do {
                UserTransaction userTransaction = new UserTransaction(cursor.getString(0), cursor.getString(1), cursor.getInt(2));
                list.add(userTransaction);
            }while (cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public User getUserTransaction(String utnumber,String fmnumber) {
        User user;

        SQLiteDatabase db = getReadableDatabase();

        sql = "Select * from "+ Table_Name_UserTransaction+ " where "+ Col_2_UTNumber +" = " + utnumber + " and " + Col_4_UTFromNumber + " = " + fmnumber;

        Cursor cursor = db.rawQuery(sql,null);
        if(cursor.moveToFirst()){
            user = new User(cursor.getString(0),cursor.getString(1),cursor.getString(2));
            Log.d("mt"," user data found: "+user.getName() + " " + user.getNumber() + " " + (user != null));
        }
        else {
            Log.d("mt","User data not found");
            return null;
        }

        cursor.close();
        db.close();

        return user;
    }


}
