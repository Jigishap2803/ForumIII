package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.forum3.R;

public class HotelUrlActivity extends AppCompatActivity {
    ImageView back_iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_hotel_url);
        back_iv = findViewById(R.id.back_iv);
        WebView hotel_webView = (WebView) findViewById(R.id.hotel_webView);
        Intent intent = getIntent();
        String hotel_url = intent.getStringExtra("hotel_url");
        hotel_webView.getSettings().setJavaScriptEnabled(true);
        hotel_webView.loadUrl(hotel_url);

        hotel_webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
}