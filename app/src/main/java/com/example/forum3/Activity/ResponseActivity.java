package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.forum3.R;

public class ResponseActivity extends AppCompatActivity {
    private TextView msg_tv,response_btn;
    private String msg = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_response);
        msg_tv = (TextView) findViewById(R.id.msg_tv);
        response_btn = (TextView) findViewById(R.id.response_btn);

        Intent intent = getIntent();
        msg = intent.getStringExtra("msg");
        msg_tv.setText(msg);

        response_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResponseActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}