package com.athleea.goodfounded;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.enableDefaults();



        checkAppFirstExecute();

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

    public void checkAppFirstExecute() {
        SharedPreferences pref = getSharedPreferences("IsFirst", Activity.MODE_PRIVATE);
        boolean isFirst = pref.getBoolean("isFirst", false);
        if (!isFirst) {
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("isFirst", true);
            editor.commit();

            Log.e("enter", "parsingStart");
            new XMLParsing(this).parsing();
        }
    }


}
