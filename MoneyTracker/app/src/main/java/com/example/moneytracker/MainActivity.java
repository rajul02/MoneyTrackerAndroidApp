package com.example.moneytracker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.WindowDecorActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements TransactionEntery.Communictaor,UpdateTransaction {


    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = (Toolbar) findViewById(R.id.appbar);
        toolbar.setTitle("MoneyTracker");
        toolbar.setLogo(getDrawable(R.mipmap.app_logo));
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setOverflowIcon(getDrawable(R.drawable.ic_add_circle_outline_black_24dp));
        setSupportActionBar(toolbar);





        UserDb db = new UserDb(getBaseContext());
        db.seeAlldata();
        User user = db.getLoginUser();
        db.close();
        if (user != null){
            Log.d("mt","User is already login: " + user.getName());
            // code of fragment if user is logined;

            MainFragment mainFragment = new MainFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragmentid,mainFragment,"MainFragment");
            transaction.commit();

        }
        else{

            LoginFragment loginFragment = new LoginFragment();
            FragmentManager manager = getSupportFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.add(R.id.fragmentid,loginFragment,"LoginFragment");
            transaction.commit();
        }


    }

    public void logout(){
        UserDb db = new UserDb(getBaseContext());
        User user = db.getLoginUser();
        db.logout(user);
        db.seeAlldata();
        db.close();

        LoginFragment frag = new LoginFragment();
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentid,frag);
        transaction.commit();

    }

    @Override
    public void respond(UserTransaction userTransaction) {
        Log.d("mt","in respond: " + userTransaction.getNumber());

        FragmentManager manager = getSupportFragmentManager();

        MainFragment mainFragment = (MainFragment) manager.findFragmentByTag("MainFragment");
        mainFragment.setUserTransactionList(userTransaction);
    }

    @Override
    public void setContact() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(getBaseContext());
        menuInflater.inflate(R.menu.actionbarmenu,menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        Log.d("mt", String.valueOf(id));

        switch (item.getItemId()){
            case R.id.add_new_record:
                UserDb userDb = new UserDb(getBaseContext());
                User user = userDb.getLoginUser();
                userDb.close();

                FragmentManager manager = getSupportFragmentManager();
                TransactionEntery transactionEntery = (TransactionEntery) manager.findFragmentByTag("TransactionFragment");

                if(transactionEntery == null){
                    transactionEntery = new TransactionEntery();
                }

                if(user != null && !transactionEntery.isVisible()) {



                    FragmentTransaction transaction = manager.beginTransaction();
                    Log.d("mt","tansaction id: " + transactionEntery);
                    transaction.add(R.id.fragmentid2, transactionEntery, "TransactionFragment");
                    transaction.addToBackStack("TransactionFragment");
                    transaction.commit();
                    return true;
                }

                return true;
            case R.id.logout:
                logout();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void updatetransaction(UserTransaction userTransaction) {
        FragmentManager manager = getSupportFragmentManager();
        TransactionEntery transactionEntery = getTraansactionEntry(userTransaction);
        Log.d("mt","transactionentryid: " + transactionEntery);
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragmentid2,transactionEntery,"TransactionEntry");
        transaction.addToBackStack("TransactionEntry");
        transaction.commit();

    }

    public TransactionEntery getTraansactionEntry(UserTransaction userTransaction){
        TransactionEntery transactionEntery = new TransactionEntery();

        Bundle bundle = new Bundle();
        bundle.putString("name",userTransaction.getName());
        bundle.putString("number",userTransaction.getNumber());
        bundle.putInt("amount",userTransaction.getAmount());
        transactionEntery.setArguments(bundle);

        return transactionEntery;
    }
}
