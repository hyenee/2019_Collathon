package com.collathon.jamukja.customer.user_info.customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.jamukja.customer.reservation.seat_confirm.ReservationSeatConfirmActivity;
import com.collathon.jamukja.customer.reservation.ticket_confirm.ReservationTicketConfirmActivity;
import com.collathon.janolja.R;

public class CustomerMyMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mymenu);

        TextView customer_info = (TextView)findViewById(R.id.customer_info);
        TextView reservation_seat_comfirm = (TextView)findViewById(R.id.reservation_seat_confirm);
        TextView reservation_ticket_comfirm = (TextView)findViewById(R.id.reservation_ticket_confirm);

        //회원정보
        customer_info.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent customerInfoIntent = new Intent(CustomerMyMenuActivity.this, CustomerInfoActivity.class);
                CustomerMyMenuActivity.this.startActivity(customerInfoIntent);

            }
        });

        //자리 예약 확인
        reservation_seat_comfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent seatIntent = new Intent(CustomerMyMenuActivity.this, ReservationSeatConfirmActivity.class);
                CustomerMyMenuActivity.this.startActivity(seatIntent);

            }
        });

        //번호표 확인
        reservation_ticket_comfirm.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent ticketIntent = new Intent(CustomerMyMenuActivity.this, ReservationTicketConfirmActivity.class);
                CustomerMyMenuActivity.this.startActivity(ticketIntent);

            }
        });

    }

}
