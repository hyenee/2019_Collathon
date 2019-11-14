package com.collathon.jamukja.customer.user_info.customer;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

public class CustomerInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info_change);

        TextView customer_name = (TextView)findViewById(R.id.customer_name);
        TextView reservation_phone = (TextView)findViewById(R.id.customer_phone);
        TextView reservation_email = (TextView)findViewById(R.id.customer_email);
        TextView reservation_id = (TextView)findViewById(R.id.customer_id);
        TextView reservation_psswd = (TextView)findViewById(R.id.customer_passwd);

    }
}
