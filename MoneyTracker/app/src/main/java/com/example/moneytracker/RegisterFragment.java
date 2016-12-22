package com.example.moneytracker;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class RegisterFragment extends Fragment implements View.OnClickListener {


    EditText name,number,pass,conf_pass;
    Button register;
    public RegisterFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register,container,false);
        name = (EditText) view.findViewById(R.id.et_Username);
        number = (EditText) view.findViewById(R.id.et_number);
        pass = (EditText) view.findViewById(R.id.et_pass);
        conf_pass = (EditText) view.findViewById(R.id.et_confirm_pass);
        register = (Button) view.findViewById(R.id.bt_reg);
        register.setOnClickListener(this);


        return view;
    }


    @Override
    public void onClick(View v) {
        if(v == register){
            Log.d("mt","Registry start " + pass.getText().toString() + " " + conf_pass.getText().toString());
            if((pass.getText().toString().equalsIgnoreCase(conf_pass.getText().toString()))
                    && (!name.getText().toString().isEmpty()) && (!number.getText().toString().isEmpty())){
                Log.d("mt","Going in Database");
                User user = new User(name.getText().toString(),number.getText().toString(),pass.getText().toString());

                UserDb userDb = new UserDb(getActivity().getBaseContext());
                userDb.addUser(user);
                userDb.seeAlldata();
                userDb.close();
                Log.d("mt","Database Closed");
                LoginFragment loginFragment = new LoginFragment();
                FragmentManager manager = getFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.fragmentid,loginFragment);
                transaction.commit();
                Log.d("mt","Transaction from register to login");

            }
            else{
                Log.d("mt","name " + !name.getText().toString().isEmpty() + !number.getText().toString().isEmpty() + (pass.getText().toString() + conf_pass.getText().toString()));
            }
        }
    }
}
