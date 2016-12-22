package com.example.moneytracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class myAdapter extends RecyclerView.Adapter<myAdapter.myViewHolder>{
    LayoutInflater inflater;
    private Context mcontext;
    UpdateTransaction updateTransaction;
    List<UserTransaction> userTransactionList = Collections.emptyList();
    long first_click = 0;
    long second_click = 0;
    static final long double_click = 500;

    myAdapter(Context context) {
        inflater=LayoutInflater.from(context);
        userTransactionList = new ArrayList<>();

    }


    @Override
    public myViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= inflater.inflate(R.layout.card,parent,false);
        myViewHolder holder=new myViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(myViewHolder holder, int position) {
        UserTransaction current=userTransactionList.get(position);
        holder.amount.setText(String.valueOf(current.getAmount()));
        holder.name.setText(current.getName().toUpperCase());
        if(current.getAmount() < 50 && current.getAmount() > 0){
            holder.amount.setTextColor(Color.rgb(0,102,34));
        }
        else if(current.getAmount() < 100 && current.getAmount() >= 50){
            holder.amount.setTextColor(Color.rgb(230,184,0));
        }
        else if(current.getAmount() >= 100){
            holder.amount.setTextColor(Color.RED);
        }
        else if(current.getAmount() < 0){
            holder.amount.setTextColor(Color.rgb(179,0,179));
        }

        holder.cv.setCardElevation(10);
        holder.cv.setCardBackgroundColor(Color.WHITE);

    }



    @Override
    public int getItemCount() {
        if(this.userTransactionList != null){
            return this.userTransactionList.size();
        }
        else {
            return 0;
        }
    }


    public class myViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        CardView cv;
        TextView name;
        TextView amount;

        public myViewHolder(View itemView) {
            super(itemView);
            mcontext = itemView.getContext();
            name= (TextView) itemView.findViewById(R.id.name);
            amount= (TextView) itemView.findViewById(R.id.amount);
            cv = (CardView) itemView.findViewById(R.id.cardviewid);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if(first_click == 0) {
                first_click = System.currentTimeMillis();
                //Log.d("mt","First_vlick: " + first_click);
            }
            else {
                second_click = System.currentTimeMillis();
              //  Log.d("mt","second_click: " + second_click);
            }

            if(((second_click - first_click) <= double_click) && (second_click - first_click) > 0){

                int position = getAdapterPosition();
                UserTransaction userTransaction = userTransactionList.get(position);

                UpdateTransaction updateTransaction = (UpdateTransaction) mcontext;
                updateTransaction.updatetransaction(userTransaction);
                first_click = 0;
                second_click = 0;
            }
            else if((second_click - first_click) > double_click){
                first_click = second_click;
                second_click = 0;
            }

           // Log.d("mt","First_vlick: " + first_click);
            //Log.d("mt","second_click: " + second_click);

        }

    }


    public void setAdatpterData(List<UserTransaction> list){
        this.userTransactionList = list;
    }
}
