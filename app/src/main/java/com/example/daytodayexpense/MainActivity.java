package com.example.daytodayexpense;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.daytodayexpense.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    private ViewPagerAdapter viewPagerAdapter;

    String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        Log.d("TAG", "onDataChange: "+email);
        Bundle bundle = new Bundle();
        bundle.putString("emails",email);
        Fragment daily = new Daily();
        daily.setArguments(bundle);
        Fragment monthly = new Monthly();
        monthly.setArguments(bundle);
        Fragment yearly = new Yearly();
        yearly.setArguments(bundle);

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());

        // add the fragments
        viewPagerAdapter.add(new notes(), "Notes");
        viewPagerAdapter.add(daily, "Daily");
        viewPagerAdapter.add(monthly, "Monthly");
        viewPagerAdapter.add(yearly, "Yearly");

        // Set the adapter
        binding.viewpager.setAdapter(viewPagerAdapter);

        // The Page (fragment) titles will be displayed in the
        // tabLayout hence we need to  set the page viewer
        // we use the setupWithViewPager().
        binding.tabLayout.setupWithViewPager(binding.viewpager);
    }
}