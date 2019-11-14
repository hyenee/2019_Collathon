package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import com.collathon.janolja.R;

public class SelectUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);
        //첫 시작 화면 -> 고객인지 자영업자인지 구분

        Button customerButton = (Button)findViewById(R.id.customerButton);
        Button ownerButton = (Button)findViewById(R.id.ownerButton);

        //고객인 경우 고객 로그인으로 넘어감
        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerIntent = new Intent(SelectUserActivity.this, LoginCustomerActivity.class);
                startActivity(customerIntent);
            }
        });

        //자영업자인 경우 자영업자 로그인으로 넘어감
        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, LoginOwnerActivity.class);
                startActivity(intent);
            }
        });


    }
}