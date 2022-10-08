package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.forum3.R;


public class ChooseLoginActivity extends AppCompatActivity {
    private TextView login_tv,register_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_choose_login);

        login_tv = (TextView)findViewById(R.id.login_tv);
        register_tv = (TextView)findViewById(R.id.register_tv);

        login_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        register_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChooseLoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}