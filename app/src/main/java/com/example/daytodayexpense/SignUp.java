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
import android.widget.Toast;

import com.example.daytodayexpense.databinding.ActivitySignUpBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    int count=0;

    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    UserInfo userInfo;

    String firstname,lastname,email,password,phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up);

       binding.signup.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               if (binding.firstname.getText().toString().isEmpty()){
                   binding.firstname.setError("Enter name");
                   return;
               }
               if (binding.lastname.getText().toString().isEmpty()){
                   binding.lastname.setError("Enter name");
                   return;
               }
               if (binding.phonenumber.getText().toString().isEmpty()){
                   binding.phonenumber.setError("Enter number");
                   return;
               }
               if (binding.email.getText().toString().isEmpty()){
                   binding.email.setError("Enter Email");
                   return;
               }
               if (binding.password.getText().toString().isEmpty()){
                   binding.password.setError("Enter password");
                   return;
               }
               firstname = binding.firstname.getText().toString();
               lastname = binding.lastname.getText().toString();
               phonenumber = binding.phonenumber.getText().toString();
               email = binding.email.getText().toString();
               password = binding.password.getText().toString();

               databaseReference = FirebaseDatabase.getInstance().getReference("users");


               addDatatoFirebase(firstname, lastname, password,phonenumber,email);



           }
       });

        binding.firstname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.firstname.setBackground(getResources().getDrawable(R.drawable.editselect));
                binding.lastname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.phonenumber.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.email.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.edit));
            }
        });


        binding.lastname.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View view, boolean b) {
                binding.firstname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.lastname.setBackground(getResources().getDrawable(R.drawable.editselect));
                binding.phonenumber.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.email.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.edit));
            }
        });
        binding.phonenumber.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.firstname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.lastname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.phonenumber.setBackground(getResources().getDrawable(R.drawable.editselect));
                binding.email.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.edit));
            }
        });
        binding.email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.firstname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.lastname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.phonenumber.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.email.setBackground(getResources().getDrawable(R.drawable.editselect));
                binding.ll.setBackground(getResources().getDrawable(R.drawable.edit));
            }
        });
        binding.password.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                binding.firstname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.lastname.setBackground(getResources().getDrawable(R.drawable.edit));
                binding.phonenumber.setBackground(getResources().getDrawable(R.drawable.edit));
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
        binding.tvsignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUp.this, SignIn.class));
            }
        });


        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.

        // initializing our object
        // class variable.
        userInfo = new UserInfo();


    }

    private void addDatatoFirebase(String firstname, String lastname, String password, String phonenumber, String email) {
        userInfo.setFirstname(firstname);
        userInfo.setLastname(lastname);
        userInfo.setPhonenumber(phonenumber);
        userInfo.setEmail(email);
        userInfo.setPassword(password);

        databaseReference.child(email.replace(".",",")).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    Toast.makeText(SignUp.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                    binding.email.setError("Email is already registered");
                    return;
                }
                // inside the method of on Data change we are setting
                // our object class to our database reference.
                // data base reference will sends data to firebase.
                databaseReference.child(email.replace(".",",")).setValue(userInfo);

                SharedPreferences preferences = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("email",email);
                editor.apply();

                // after adding this data we are showing toast message.
                Toast.makeText(SignUp.this, "data added", Toast.LENGTH_SHORT).show();
                Daily daily = new Daily();
                daily.setData(email);
                startActivity(new Intent(SignUp.this, MainActivity.class).putExtra("email",email));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // if the data is not added or it is cancelled then
                // we are displaying a failure toast message.
                Toast.makeText(SignUp.this, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }


}