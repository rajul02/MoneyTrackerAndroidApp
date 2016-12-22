package com.example.moneytracker;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MainFragment extends Fragment{

    EditText name,amount,number;
    Button btn;


    List<UserTransaction> userTransactionList = new ArrayList();
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    myAdapter adapter;

    List<UserTransaction> list;

    public MainFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);


        UserDb userDb = new UserDb(getActivity().getBaseContext());
        userTransactionList = userDb.getAllTransaction();
        userDb.close();

        this.adapter = new myAdapter(getActivity().getBaseContext());


       // Log.d("mt","adapter set: " + adapter);
        adapter.setAdatpterData(userTransactionList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    public  void setUserTransactionList(UserTransaction userTransaction){
        if(userTransactionList != null){

            //myAdapter adapter = new myAdapter(getActivity().getBaseContext());
        //    Log.d("mt","List not null: " + adapter);
            UserDb userDb = new UserDb(getActivity().getBaseContext());
            userTransactionList = userDb.getAllTransaction();
            adapter.setAdatpterData(userTransactionList);
            adapter.notifyDataSetChanged();
        }
        else {
         //   Log.d("mt","List is null idiot");

        }
    }

}
