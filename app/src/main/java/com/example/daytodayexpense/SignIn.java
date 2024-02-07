package com.example.daytodayexpense;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import com.example.daytodayexpense.databinding.ActivitySignInBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    int count=0;
    String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_sign_in);

        SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        email = preferences.getString("email", null);


        if (email!=null) {
            // If logged in, directly navigate to the main activity
            Intent intent = new Intent(SignIn.this, MainActivity.class);
            intent.putExtra("email",email);
            startActivity(intent);
        }


        binding.email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.email.setBackground(getResources().getDrawable(R.drawable.editselect));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.edit));
            }
        });
        binding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.email.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.editselect));
            }
        });

        binding.eye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (count == 0) {
                    binding.eye.setImageResource(R.drawable.hideeye);
                    binding.password.setInputType(InputType.TYPE_CLASS_TEXT);
                    count = 1;
                }
                else {
                    count = 0;
                    binding.eye.setImageResource(R.drawable.eye);
                    binding.password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
            }
        });
        binding.tvsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignIn.this, SignUp.class));
            }
        });
        binding.signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUser();
            }
        });
    }

    public void checkUser(){
        email = binding.email.getText().toString().trim();
        String userPassword = binding.password.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String passwordFromDB = snapshot.child(email.replace(".",",")).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {

//                        setLoggedIn(true);


                        SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("email",email);
                        editor.apply();

                        Daily daily = new Daily();
                        daily.setData(email);
                        Intent intent = new Intent(SignIn.this, MainActivity.class);
                        intent.putExtra("email",email);
                        startActivity(intent);
                    } else {
                        binding.password.setError("Invalid Credentials");
                        binding.password.requestFocus();
                    }
                } else {
                    binding.email.setError("User does not exist");
                    binding.email.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


}