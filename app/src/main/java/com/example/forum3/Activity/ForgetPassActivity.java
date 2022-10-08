package com.example.forum3.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.forum3.R;

public class ForgetPassActivity extends AppCompatActivity {
    private EditText email_edt;
    private TextView send_otp_tv;
    private String email_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_forget_pass);
        idCalling();
        click_event();

    }

    private void idCalling(){
        email_edt = (EditText) findViewById(R.id.email_edt);
        send_otp_tv = (TextView) findViewById(R.id.send_otp_tv);
    }

    private void click_event(){
        send_otp_tv.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick ( View view ) {
                email_id = email_edt.getText().toString().trim();
                if(email_id.equalsIgnoreCase("")){
                    Toast.makeText(ForgetPassActivity.this,"Please enter valid email id",Toast.LENGTH_SHORT).show();
                }else{

                }
            }
        } );
    }

}