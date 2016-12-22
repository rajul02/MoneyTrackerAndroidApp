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
import android.widget.Toast;


public class LoginFragment extends Fragment implements View.OnClickListener {

    EditText phone_number, password;
    Button login,register;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login,container,false);

        phone_number = (EditText) view.findViewById(R.id.et_phone_number);
        password = (EditText) view.findViewById(R.id.et_password);
        login = (Button) view.findViewById(R.id.bt_login);
        register = (Button) view.findViewById(R.id.bt_register);
        login.setOnClickListener(this);
        register.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        if(v == login){
            String number = phone_number.getText().toString();
            String pass = password.getText().toString();
            if(!number.isEmpty() && !pass.isEmpty()) {
                UserDb userDb = new UserDb(getActivity().getBaseContext());
                if (userDb.login(number, pass)) {
                    MainFragment mainFragment = new MainFragment();
                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.fragmentid, mainFragment, "MainFragment");
                    transaction.commit();
                } else {
                    Toast.makeText(getActivity().getBaseContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                    phone_number.setText("");
                    password.setText("");
                }
            }
            else {
                Toast.makeText(getActivity().getBaseContext(),"Please fill Login Details Correctly",Toast.LENGTH_SHORT).show();
            }
        }
        else if(v == register){
            RegisterFragment registerFragment = new RegisterFragment();
            FragmentManager manager = getFragmentManager();
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.replace(R.id.fragmentid,registerFragment);
            transaction.addToBackStack("RegisterFragment");
            transaction.commit();

        }

    }
}
