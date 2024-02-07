package com.example.daytodayexpense;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daytodayexpense.databinding.ActivityMonthparentBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {
    List<String> transactions;
    List<Transaction> transactionsc;
    Calendar calendar;
    String transaction;
    SimpleDateFormat currentmonth;
    SimpleDateFormat currentyear;
    String email;
    DatabaseReference databaseReference1;

    Context context;

    public CustomAdapter(SimpleDateFormat currentmonth, SimpleDateFormat currentyear, String email) {
        this.transactions = new ArrayList<>();
        this.transactionsc = new ArrayList<>();
        this.currentyear = currentyear;
        this.currentmonth = currentmonth;
        this.email = email;
        this.calendar = Calendar.getInstance();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_monthparent, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {
        if (transactions != null) {
            transaction = transactions.get(position);
            transactionsc.clear();
            holder.bind(transaction);
            holder.binding.date.setText(transaction+currentmonth.format(calendar.getTime())+currentyear.format(calendar.getTime()));

        }


    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public void setTransactionList(List<String> transactions) {
        this.transactions = transactions;
        notifyDataSetChanged();
    }

    public void updateMonth(Calendar newCalendar) {
        this.calendar = newCalendar;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ActivityMonthparentBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActivityMonthparentBinding.bind(itemView);
        }

        public void bind(String transaction) {


            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users")
                    .child(email.replace(".", ","))
                    .child(currentyear.format(calendar.getTime()))
                    .child(currentmonth.format(calendar.getTime()));


            List<Transaction> transactionListForDate = new ArrayList<>();
            databaseReference1.child(transaction).child("Cedit").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    String des = snapshot.getKey().toString();
                    String val = snapshot.getValue().toString();
                    Transaction transaction = new Transaction(val, des);
                    transactionListForDate.add(transaction);
                    TransactionAdapter creditAdapter = new TransactionAdapter(context, R.layout.activity_data, transactionListForDate);

                    binding.lvcredit.setAdapter(creditAdapter);

                    setListViewHeight(binding.lvcredit);
                    Log.d("TAG", "onDataChange: " + transactionsc);
                    creditAdapter.notifyDataSetChanged();

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

            List<Transaction> transactionListForDated = new ArrayList<>();
            // Fetch Debit transactions
            databaseReference1.child(transaction).child("debit").addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String des = snapshot.getKey().toString();
                    String val = snapshot.getValue().toString();
                    Transaction transaction = new Transaction(val, des);
                    transactionListForDated.add(transaction);
                    TransactionAdapter debitAdapter = new TransactionAdapter(context, R.layout.activity_data, transactionListForDated);

                    binding.lvdebit.setAdapter(debitAdapter);

                    setListViewHeight(binding.lvdebit);
                    Log.d("TAG", "onDataChange: " + transactionsc);
                    debitAdapter.notifyDataSetChanged();
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

//            transactionsc.add(transaction);
//            TransactionAdapter creditAdapter = new TransactionAdapter(context, R.layout.activity_data, transactionsc);
//            binding.lvcredit.setAdapter(creditAdapter);
//
//            // Check transaction type and set appropriate color or other attributes
//            if ("Credit".equals(transaction.getType())) {
//
//            } else if ("Debit".equals(transaction.getType())) {
//                // Set debit-specific styling
//            }
        }
    }
    private void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }
}
