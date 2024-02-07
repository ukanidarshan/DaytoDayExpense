package com.example.daytodayexpense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpense.databinding.FragmentMonthlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Monthly extends Fragment {

    FragmentMonthlyBinding binding;
    List<String> transactionList = new ArrayList<>();
CustomAdapter adapter;
    Calendar calendar;
    SimpleDateFormat simpleDateFormat,currentmonth,currentyear;
    String Datetime,email;

    public Monthly() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        calendar = Calendar.getInstance();
//        currentmonth = new SimpleDateFormat("LLLL");
//        currentyear = new SimpleDateFormat("yyyy");
        transactionList.clear();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_monthly,container,false);
        // Inflate the layout for this fragment

        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("emails")){
            email = bundle.getString("emails");
//            adapter = new CustomAdapter(currentmonth,currentyear,email);
//            adapter.notifyDataSetChanged();

            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("LLLL-yyyy");
            Datetime = simpleDateFormat.format(calendar.getTime()).toString();
            currentmonth = new SimpleDateFormat("LLLL");
            currentyear = new SimpleDateFormat("yyyy");
            adapter = new CustomAdapter(currentmonth,currentyear,email);

//            adapter.setCurrentmonth();
            fetchData();
            binding.month.setText(Datetime);

            binding.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.MONTH, -1);
                    updateMonth();
                }
            });
            binding.forw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.MONTH, 1);
                    updateMonth();
                }
            });

        }



    }
    public void fetchData() {


        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users")
                .child(email.replace(".", ","))
                .child(currentyear.format(calendar.getTime()))
                .child(currentmonth.format(calendar.getTime()));
        transactionList.clear();
        Log.d("TAG", "fetchData: "+currentmonth.format(calendar.getTime()));


        binding.monrcv.setAdapter(adapter);
        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String date = snapshot.getKey();


                transactionList.add(date);
               adapter.setTransactionList(transactionList);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "Error fetching dates", error.toException());
            }
        });
    }
    private void updateMonth() {
        simpleDateFormat = new SimpleDateFormat("LLLL-yyyy");
        Datetime = simpleDateFormat.format(calendar.getTime()).toString();
        currentmonth = new SimpleDateFormat("LLLL");
        currentyear = new SimpleDateFormat("yyyy");
        adapter.updateMonth(calendar);
        fetchData();
        binding.month.setText(Datetime);
    }
}