package com.example.daytodayexpense;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.daytodayexpense.databinding.ActivityDataBinding;

import java.util.List;

public class AdapterClass extends RecyclerView.Adapter<AdapterClass.ViewHolder> {
    List<Transaction> es;

    public AdapterClass(List<Transaction> es) {
        this.es = es;

    }

    @NonNull
    @Override
    public AdapterClass.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_data,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterClass.ViewHolder holder, int position) {
        Transaction transaction = es.get(position);
        holder.bind(transaction);

    }

    @Override
    public int getItemCount() {
        return es.size();
    }
    public void setTransactionList(List<Transaction> transactions) {
        es = transactions;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ActivityDataBinding binding;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ActivityDataBinding.bind(itemView);
        }

        public void bind(Transaction transaction) {
            binding.des.setText(transaction.getAmount());
            binding.val.setText(transaction.getDescription());
        }
    }
}
