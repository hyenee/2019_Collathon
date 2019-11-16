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

        Button customerButton = (Button)findViewById(R.id.customerButton);

        customerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent customerIntent = new Intent(SelectUserActivity.this, LoginCustomerActivity.class);
                startActivity(customerIntent);
            }
        });

        Button ownerButton = (Button)findViewById(R.id.ownerButton);

        ownerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SelectUserActivity.this, LoginOwnerActivity.class);
                startActivity(intent);
            }
        });


    }
}