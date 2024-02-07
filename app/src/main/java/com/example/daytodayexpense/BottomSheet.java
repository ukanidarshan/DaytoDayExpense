package com.example.daytodayexpense;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.daytodayexpense.databinding.FragmentBottomSheetBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BottomSheet extends BottomSheetDialogFragment {

  FragmentBottomSheetBinding binding;




  DatabaseReference databaseReference;

  int count=0;

    public BottomSheet() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_bottom_sheet,container,false);




        String Email = getArguments().getString("Email","").replace(".",",");
        String Date = getArguments().getString("Date","");
        String Month = getArguments().getString("Month","");
        String Year = getArguments().getString("Year","");
        // Inflate the layout for this fragment

        binding.income.setClickable(true);
        binding.income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count =0;
                binding.income.setBackground(getResources().getDrawable(R.drawable.buttonshape));
                binding.expense.setBackgroundResource(0);
            }
        });
        binding.expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                count = 1;
                binding.expense.setBackground(getResources().getDrawable(R.drawable.buttonright));
                binding.income.setBackgroundResource(0);
            }
        });
        binding.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = binding.description.getText().toString();
                String amountText = binding.value.getText().toString();
                if (text.isEmpty()){
                    binding.description.setError("Enter description");
                    return;
                }
                if (amountText.isEmpty()){
                    binding.value.setError("Enter value");
                    return;
                }
                if (count==0) {
                    FirebaseDatabase.getInstance().getReference("users").child(Email).child(Year).child(Month).child(Date).child("Cedit").child(text).setValue(amountText);
                    binding.description.setText("");
                    binding.value.setText("");
                }
                else {
                    FirebaseDatabase.getInstance().getReference("users").child(Email).child(Year).child(Month).child(Date).child("debit").child(text).setValue(amountText);
                    binding.description.setText("");
                    binding.value.setText("");
                }

            }
        });
        return binding.getRoot();
    }
}