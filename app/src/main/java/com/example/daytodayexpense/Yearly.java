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

import com.example.daytodayexpense.databinding.FragmentYearlyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Yearly extends Fragment {

    FragmentYearlyBinding binding;

    Calendar calendar;

    int income;
    SimpleDateFormat simpleDateFormat, currentyear;

    List<Integer> Incomelist = new ArrayList<>();
    String Datetime, email;
    int balc, bald;

    YearAdapter yearAdapter;

    List<String> month = new ArrayList<>();

    public Yearly() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_yearly, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("emails")) {
            email = bundle.getString("emails");
            calendar = Calendar.getInstance();
            simpleDateFormat = new SimpleDateFormat("yyyy");
            Datetime = simpleDateFormat.format(calendar.getTime()).toString();
            currentyear = new SimpleDateFormat("yyyy");
            binding.year.setText(Datetime);

            fetchData();

            binding.back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.YEAR, -1);
                    simpleDateFormat = new SimpleDateFormat("yyyy");
                    Datetime = simpleDateFormat.format(calendar.getTime()).toString();
                    currentyear = new SimpleDateFormat("yyyy");
                    fetchData();
                    binding.year.setText(Datetime);
                }
            });
            binding.forw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    calendar.add(Calendar.YEAR, 1);
                    simpleDateFormat = new SimpleDateFormat("yyyy");
                    Datetime = simpleDateFormat.format(calendar.getTime()).toString();
                    currentyear = new SimpleDateFormat("yyyy");
                    fetchData();
                    binding.year.setText(Datetime);
                }
            });
            binding.yearrcv.setAdapter(yearAdapter);
        }


    }

    public void fetchData() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users")
                .child(email.replace(".", ","))
                .child(currentyear.format(calendar.getTime()));

        List<String> month = new ArrayList<>();

        yearAdapter = new YearAdapter(email,currentyear);

        databaseReference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String currentmonth = snapshot.getKey();
                month.add(currentmonth);
                yearAdapter.setMonth(month);
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
            }
        });
    }

}