package com.collathon.jamukja.customer.user_info.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.jamukja.customer.reservation.confirm.ReservationConfirmActivity;
import com.collathon.janolja.R;

public class CustomerMyMenuActivity extends AppCompatActivity {

    private static final String TAG = "CustomerMyMenuActivity";
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mymenu);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");

        TextView customer_info = (TextView)findViewById(R.id.customer_info);
        TextView reservation_ticket_comfirm = (TextView)findViewById(R.id.reservation_confirm);

        //회원정보
        customer_info.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent customerInfoIntent = new Intent(CustomerMyMenuActivity.this, CustomerInfoActivity.class);
                customerInfoIntent.putExtra("userID", userID);
                CustomerMyMenuActivity.this.startActivity(customerInfoIntent);

            }
        });


        //번호표 확인
        reservation_ticket_comfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent ticketIntent = new Intent(CustomerMyMenuActivity.this, ReservationConfirmActivity.class);
                ticketIntent.putExtra("userID", userID);
                CustomerMyMenuActivity.this.startActivity(ticketIntent);

            }
        });

    }

}
