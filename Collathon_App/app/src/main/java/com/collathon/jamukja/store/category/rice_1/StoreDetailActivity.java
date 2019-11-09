package com.collathon.jamukja.store.category.rice_1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

public class StoreDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);
        final Button menuButton = (Button)findViewById(R.id.menuButton);
        final Button infoButton = (Button)findViewById(R.id.store_informationButton);

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(StoreDetailActivity.this, StoreDetailInfoActivity.class);
                StoreDetailActivity.this.startActivity(menuIntent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(StoreDetailActivity.this, StoreDetailInfoActivity.class);
                StoreDetailActivity.this.startActivity(infoIntent);
            }
        });

    }
}
