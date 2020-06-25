package com.athleea.goodfounded;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecommendFood extends AppCompatActivity {

    private static final int SEARCH_ADDRESS_ACTIVITY = 10000;


    private TextView txt_address;
    Button address;
    Button recommend;
    CheckBox[] foodList = new CheckBox[15];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodrecommend);

        initView();


        address.setOnClickListener(view -> {
            Intent i = new Intent(RecommendFood.this, WebViewActivity.class);
            startActivityForResult(i, SEARCH_ADDRESS_ACTIVITY);
        });
        recommend.setOnClickListener(view -> {

        });
    }

    void initView() {
        txt_address = findViewById(R.id.txt_address);
        address = findViewById(R.id.addressBtn);
        recommend = findViewById(R.id.recommendAddress);

        for (int i = 0; i < foodList.length; i++) {
            String n = "list" + i;
            int resID = getResources().getIdentifier(n, "id", getPackageName());

            foodList[i] = findViewById(resID);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {

        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == SEARCH_ADDRESS_ACTIVITY && resultCode == RESULT_OK) {
            String data = intent.getExtras().getString("data");
            if (data != null)
                txt_address.setText(data);

        }
    }
}