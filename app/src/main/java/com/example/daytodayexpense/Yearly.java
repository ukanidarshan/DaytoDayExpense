package com.example.daytodayexpense;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpense.databinding.FragmentYearlyBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Yearly extends Fragment {

    FragmentYearlyBinding binding;

    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String Datetime;

    public Yearly() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_yearly,container,false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("yyyy");
        Datetime = simpleDateFormat.format(calendar.getTime()).toString();
        binding.year.setText(Datetime);

        binding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.YEAR,-1);
                simpleDateFormat = new SimpleDateFormat("yyyy");
                Datetime = simpleDateFormat.format(calendar.getTime()).toString();
                binding.year.setText(Datetime);
            }
        });
        binding.forw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.add(Calendar.YEAR,1);
                simpleDateFormat = new SimpleDateFormat("yyyy");
                Datetime = simpleDateFormat.format(calendar.getTime()).toString();
                binding.year.setText(Datetime);
            }
        });

    }
}