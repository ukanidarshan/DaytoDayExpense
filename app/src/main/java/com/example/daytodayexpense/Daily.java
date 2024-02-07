package com.example.daytodayexpense;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.daytodayexpense.databinding.FragmentDailyBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Daily extends Fragment {
    private FragmentDailyBinding binding;
    private Calendar calendar;
    private SimpleDateFormat simpleDateFormat, currentdate, currentmonth, currentyear;
    private String Datetime, email;
    List<Transaction> transactionList = new ArrayList<>();
    List<Transaction> transactionListd = new ArrayList<>();
    AdapterClass adapterClass = new AdapterClass(transactionList);
    AdapterClass adapterClassd = new AdapterClass(transactionListd);

    int bal,balc,mainbal;


    public Daily() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        calendar = Calendar.getInstance();
        currentdate = new SimpleDateFormat("dd");
        currentmonth = new SimpleDateFormat("LLLL");
        currentyear = new SimpleDateFormat("yyyy");


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_daily, container, false);
        // Inflate the layout for this fragment
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




        Bundle bundle = getArguments();
        if (bundle != null && bundle.containsKey("emails")) {
            email = bundle.getString("emails");
            bal =0;
            binding.balanced.setText("0.00");
            balc =0;
            binding.balancec.setText("0.00");
            mainbal =0;
            binding.balance.setText("0.00");
            fetchData();
            binding.balance.setText(String.valueOf(mainbal));


            simpleDateFormat = new SimpleDateFormat("dd");
            Datetime = simpleDateFormat.format(calendar.getTime());
            binding.date.setText(Datetime);

            currentdate = new SimpleDateFormat("dd");
            currentmonth = new SimpleDateFormat("LLLL");
            currentyear = new SimpleDateFormat("yyyy");


            simpleDateFormat = new SimpleDateFormat("LLLL-yyyy");
            Datetime = simpleDateFormat.format(calendar.getTime());
            binding.month.setText(Datetime);

            simpleDateFormat = new SimpleDateFormat("EEEE");
            Datetime = simpleDateFormat.format(calendar.getTime());
            binding.day.setText(Datetime);

            binding.balance.setText("0000");

            // Update the user's data in Firebase Realtime Database

            binding.back.setOnClickListener(v -> updateDate(-1));
            binding.forw.setOnClickListener(v -> updateDate(1));

            binding.recylerc.setAdapter(adapterClass);
            binding.recylerd.setAdapter(adapterClassd);

            binding.add.setOnClickListener(v -> {
                BottomSheet bottomSheet = new BottomSheet();

                currentdate = new SimpleDateFormat("dd");
                currentmonth = new SimpleDateFormat("LLLL");
                currentyear = new SimpleDateFormat("yyyy");

                Bundle bundle1 = new Bundle();
                bundle1.putString("Email",email);
                bundle1.putString("Date",currentdate.format(calendar.getTime()));
                bundle1.putString("Month",currentmonth.format(calendar.getTime()));
                bundle1.putString("Year",currentyear.format(calendar.getTime()));
                bottomSheet.setArguments(bundle1);
                bottomSheet.show(getFragmentManager(), bottomSheet.getTag());
            });
        } else {
            Log.e("TAG", "Bundle is null or email key is missing.");
        }
    }

    public void fetchData() {
        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users")
                .child(email.replace(".", ","))
                .child(currentyear.format(calendar.getTime()))
                .child(currentmonth.format(calendar.getTime()))
                .child(currentdate.format(calendar.getTime()));

        transactionList.clear();

        // Fetch Credit transactions
        databaseReference1.child("Cedit").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                String des = snapshot.getKey().toString();
                String val = snapshot.getValue().toString();
                Transaction transaction = new Transaction(des, val);
                transactionList.add(transaction);
                int valu = Integer.parseInt(val);
                balc = balc+valu;
                mainbal = balc-bal;
                binding.balancec.setText(String.valueOf(balc));


                Log.d("TAG", "onDataChange: "+transactionList);


                adapterClass.setTransactionList(transactionList);
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
        transactionListd.clear();

        // Fetch Debit transactions
        databaseReference1.child("debit").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String des = snapshot.getKey().toString();
                String val = snapshot.getValue().toString();
                Transaction transaction = new Transaction(des, val);
                int valu = Integer.parseInt(val);
                bal = bal+valu;
                binding.balanced.setText(String.valueOf(bal));
                transactionListd.add(transaction);
                mainbal = balc-bal;

                Log.d("TAG", "debti: "+transactionListd);


                adapterClassd.setTransactionList(transactionListd);
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

    private void updateDate(int daysToAdd) {
        calendar.add(Calendar.DATE, daysToAdd);
        simpleDateFormat = new SimpleDateFormat("dd");
        Datetime = simpleDateFormat.format(calendar.getTime());
        binding.date.setText(Datetime);

        currentdate = new SimpleDateFormat("dd");
        currentmonth = new SimpleDateFormat("LLLL");
        currentyear = new SimpleDateFormat("yyyy");


        transactionListd.clear();
        adapterClass.setTransactionList(transactionList);
        transactionList.clear();
        adapterClassd.setTransactionList(transactionListd);

        bal = 0;
        binding.balanced.setText("0.00");
        balc = 0;
        binding.balancec.setText("0.00");
        mainbal = 0;
        binding.balance.setText("0.00");
        fetchData();
        binding.balance.setText(String.valueOf(mainbal));

        simpleDateFormat = new SimpleDateFormat("LLLL-yyyy");
        Datetime = currentmonth.format(calendar.getTime());
        binding.month.setText(Datetime);

        simpleDateFormat = new SimpleDateFormat("EEEE");
        Datetime = simpleDateFormat.format(calendar.getTime());
        binding.day.setText(Datetime);

    }


    public void setData(String email) {
        this.email = email.replace(".", ",");
    }
}
