package com.example.daytodayexpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.daytodayexpense.databinding.ActivityYearinfoBinding;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class YearAdapter extends RecyclerView.Adapter<YearAdapter.ViewHolder> {
    List<String> month;
    Context context;
    List<Integer> income;
    String email;
    int expense;
    SimpleDateFormat currentyear;

    Calendar calendar;


    public YearAdapter(String email, SimpleDateFormat currentyear) {
        this.email = email;
        this.currentyear = currentyear;
        this.month = new ArrayList<>();
        calendar = Calendar.getInstance();
    }

    @NonNull
    @Override
    public YearAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_yearinfo, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearAdapter.ViewHolder holder, int position) {
            holder.setData(position);
    }

    @Override
    public int getItemCount() {
        return month.size();
    }

    public void setMonth(List<String> month) {
        this.month = month;
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        ActivityYearinfoBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActivityYearinfoBinding.bind(itemView);
        }

        public void setData(int position) {

            DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("users")
                    .child(email.replace(".", ","))
                    .child(currentyear.format(calendar.getTime()));


            databaseReference1.child(month.get(position)).addChildEventListener(new ChildEventListener() {
                int balc = 0;
                int bald = 0;
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    String currentdate = snapshot.getKey();

                    databaseReference1.child(month.get(position)).child(currentdate).child("Cedit").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String val = snapshot.getValue().toString();
                            int valu = Integer.parseInt(val);
                            balc = balc + valu;
                            binding.inc.setText(String.valueOf(balc));
                            binding.bal.setText(String.valueOf(balc-bald));
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

                    databaseReference1.child(month.get(position)).child(currentdate).child("debit").addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            String val = snapshot.getValue().toString();
                            int valu = Integer.parseInt(val);
                            bald = bald + valu;
                            binding.exp.setText(String.valueOf(bald));
                            binding.bal.setText(String.valueOf(balc-bald));
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

            binding.mon.setText(month.get(position));

        }
    }
}
