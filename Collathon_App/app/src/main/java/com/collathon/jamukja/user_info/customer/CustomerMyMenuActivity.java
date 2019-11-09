package com.collathon.jamukja.user_info.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.jamukja.RegisterCustomerActivity;
import com.collathon.jamukja.store.category.rice_1.StoreDetailReserActivity;
import com.collathon.janolja.R;

public class CustomerMyMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mymenu);

        TextView customer_info = (TextView)findViewById(R.id.customer_info);
        TextView reservation_comrirm = (TextView)findViewById(R.id.reservation_confirm);

        customer_info.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent customerInfoIntent = new Intent(CustomerMyMenuActivity.this, CustomerInfoActivity.class);
                CustomerMyMenuActivity.this.startActivity(customerInfoIntent);

            }
        });


        reservation_comrirm.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent reservationIntent = new Intent(CustomerMyMenuActivity.this, StoreDetailReserActivity.class);
                CustomerMyMenuActivity.this.startActivity(reservationIntent);

            }
        });

    }

}
