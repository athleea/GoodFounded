package com.athleea.goodfounded;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();

    private FragmentHome homeFragment = new FragmentHome();
    private FragmentChart chartFragment = new FragmentChart();
    private FragmentMap mapFragment = new FragmentMap();
    private FragmentSeeMore seemoreFragment = new FragmentSeeMore();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //하단네비게이션탭
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.nav_menu1: {
                        transaction.replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.nav_menu2: {
                        transaction.replace(R.id.main_layout, chartFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.nav_menu3: {
                        transaction.replace(R.id.main_layout, mapFragment).commitAllowingStateLoss();
                        break;
                    }
                    case R.id.nav_menu4: {
                        transaction.replace(R.id.main_layout, seemoreFragment).commitAllowingStateLoss();
                        break;
                    }
                }

                return true;
            }
        });

    }
}
