package com.example.daytodayexpense;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TransactionAdapter extends ArrayAdapter<Transaction> {

    private Context context;
    private int resource;

    public TransactionAdapter(Context context, int resource, List<Transaction> transactions) {
        super(context, resource, transactions);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
        }

        Transaction transaction = getItem(position);

        if (transaction != null) {
            // Set the data to your TextViews or other views in your layout
            TextView descriptionTextView = convertView.findViewById(R.id.des);
            TextView valueTextView = convertView.findViewById(R.id.val);

            if (descriptionTextView != null) {
                descriptionTextView.setText(transaction.getDescription());
            }

            if (valueTextView != null) {
                valueTextView.setText(transaction.getAmount());
            }
        }

        return convertView;
    }
}
