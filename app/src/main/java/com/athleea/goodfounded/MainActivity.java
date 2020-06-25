package com.athleea.goodfounded;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentHome homeFragment = new FragmentHome();
    private FragmentChart chartFragment = new FragmentChart();
    private FragmentMap mapFragment = new FragmentMap();
    private FragmentSeeMore moreFragment = new FragmentSeeMore();

    static final String TAG = "database";
    static SQLiteDatabase database;
    DatabaseHelper helper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        StrictMode.enableDefaults();
        patchEOFException();


        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();






        //하단네비게이션탭
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            FragmentTransaction transaction1 = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.nav_menu1:
                    transaction1.replace(R.id.main_layout, homeFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_menu2:
                    transaction1.replace(R.id.main_layout, chartFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_menu3:
                    transaction1.replace(R.id.main_layout, mapFragment).commitAllowingStateLoss();
                    break;

                case R.id.nav_menu4:
                    transaction1.replace(R.id.main_layout, moreFragment).commitAllowingStateLoss();
                    break;
                default:
                    break;
            }

            return true;
        });

    }



    private void patchEOFException() {
        System.setProperty("http.keepAlive", "false");
    }



}
