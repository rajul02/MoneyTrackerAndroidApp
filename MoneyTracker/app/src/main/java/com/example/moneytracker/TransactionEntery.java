package com.example.moneytracker;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class TransactionEntery extends Fragment implements View.OnClickListener {

    Button button;
    EditText name;
    EditText amount;
    EditText number;
    Button getcontact;
    String contact_name;
    String contact_number;
    int contact_amount;
    Communictaor communictaor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view=inflater.inflate(R.layout.fragment_transaction_entery, container, false);
        button= (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(this);
        name= (EditText) view.findViewById(R.id.name);
        amount= (EditText) view.findViewById(R.id.amount);
        number= (EditText) view.findViewById(R.id.mobno);

        Bundle args = getArguments();
        if(args != null) {
            Log.d("mt", "trentry args: " + args);
            contact_name = args.getString("name");
            contact_number = args.getString("number");
            contact_amount = args.getInt("amount");
            if(!contact_name.isEmpty() && !contact_number.isEmpty()){
                name.setText(contact_name);
                number.setText(contact_number);
                amount.setText(String.valueOf(contact_amount));
            }
        }
        else {
            name.setText("");
            number.setText("");
            amount.setText("");
        }

        getcontact = (Button) view.findViewById(R.id.btn_getContact);
        getcontact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v == getcontact){

                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent,1);
                }
            }
        });

        return view;
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if ((resultCode == Activity.RESULT_OK)) {
                Uri contact = data.getData();
                ContentResolver resolver = getActivity().getContentResolver();
                Cursor c = resolver.query(contact,null,null,null,null);
                if(c.moveToFirst()){
                    contact_name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    contact_number = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceFirst("\\+","").replaceAll("-","").replaceAll(" ","");

                }
                c.close();
            }
            name.setText(contact_name);
            number.setText(contact_number);
           }
    }

    @Override
    public void onClick(View v) {
        if(v==button){
            List<UserTransaction> userTransactionList=new ArrayList();

            String utname = name.getText().toString();
            String utnumber = number.getText().toString();
            String val = amount.getText().toString();
            if(!utname.isEmpty() && !utnumber.isEmpty() && !val.isEmpty()) {
                UserTransaction userTransaction = new UserTransaction(utname, utnumber,Integer.parseInt(val));

                UserDb userDb = new UserDb(getActivity().getBaseContext());
                userDb.addUserTransaction(userTransaction);
                userDb.close();

                userTransaction = new UserTransaction(utname, utnumber, Integer.parseInt(val));
                if (userTransaction != null) {
                    Log.d("mt", "i am lol: " + userTransaction.getNumber());
                }
                communictaor = (Communictaor) getContext();
                communictaor.respond(userTransaction);
            }
            else {
                Toast.makeText(getActivity().getBaseContext(),"Please fill All the Details",Toast.LENGTH_SHORT).show();
            }

            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(this);
            transaction.commit();

        }
    }
    public  interface Communictaor{
        public void respond(UserTransaction userTransaction);

        public void setContact();
    }

}
