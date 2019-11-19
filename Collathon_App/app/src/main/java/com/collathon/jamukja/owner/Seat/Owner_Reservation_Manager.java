package com.collathon.jamukja.owner.Seat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.collathon.janolja.R;

public class Owner_Reservation_Manager extends AppCompatActivity {

    private TextView textView_information,textView_seat,textView_ticket;
    String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_reservation_manager);
        final Intent intent = getIntent();
        ownerID = intent.getExtras().getString("owner_id");
        Log.i("STORE","매니저"+ownerID);

        textView_information = findViewById(R.id.et_info);
        textView_seat = findViewById(R.id.et_numberOfseat);
        textView_ticket = findViewById(R.id.et_numberOfticket);

        textView_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //////////////////////////////혜이니꺼 처럼 수정하기/////////////////////////////////
            }
        });
        textView_seat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Owner_Reservation_Manager.this,Owner_Reservation_seat.class);
                intent.putExtra("owner_id", ownerID);
                startActivity(intent);
            }
        });

    }
}
