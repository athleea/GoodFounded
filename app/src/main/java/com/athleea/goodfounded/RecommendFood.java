package com.athleea.goodfounded;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecommendFood extends AppCompatActivity {

    WebView webView;
    private TextView txt_address;
    private Handler handler;
    Button address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_foodrecommend);

        txt_address = findViewById(R.id.txt_address);

        address = findViewById(R.id.addressBtn);
        address.setOnClickListener(view -> {
            init_webView();
            handler = new Handler();
        });
    }

    public void init_webView() {
        // WebView 설정
        webView = findViewById(R.id.webView_address);

        // JavaScript 허용
        webView.getSettings().setJavaScriptEnabled(true);

        // JavaScript의 window.open 허용
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

        // JavaScript이벤트에 대응할 함수를 정의 한 클래스를 붙여줌
        webView.addJavascriptInterface(new AndroidBridge(), "TestApp");

        // web client 를 chrome 으로 설정
        webView.setWebChromeClient(new WebChromeClient());

        // webview url load. php 파일 주소
        webView.loadUrl("file:///android_asset/www/test.html");

    }


    private class AndroidBridge {
        @JavascriptInterface
        public void setAddress(final String arg1, final String arg2, final String arg3) {
            handler.post(() -> {
                txt_address.setText(String.format("(%s) %s %s", arg1, arg2, arg3));
                // WebView를 초기화 하지않으면 재사용할 수 없음
                init_webView();
            });
        }
    }


}