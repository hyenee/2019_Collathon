package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.collathon.jamukja.store.category.chicken_3.ChickenActivity;
import com.collathon.jamukja.store.category.desert_5.DesertActivity;
import com.collathon.jamukja.store.category.meat_4.MeatActivity;
import com.collathon.jamukja.store.category.noodle_2.NoodleActivity;
import com.collathon.jamukja.store.category.rice_1.RiceActivity;
import com.collathon.jamukja.store.category.sushi_6.SushiActivity;
import com.collathon.jamukja.user_info.customer.CustomerMyMenuActivity;
import com.collathon.janolja.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //카테고리 버튼 클릭하면 이동, 순서대로 버튼 1~6
        ImageButton riceButton = findViewById(R.id.category_button_rice);
        ImageButton noodleButton = findViewById(R.id.category_button_noodle);
        ImageButton chickenButton = findViewById(R.id.category_button_chicken);
        ImageButton meatButton = findViewById(R.id.category_button_meat);
        ImageButton desertButton = findViewById(R.id.category_button_desert);
        ImageButton sushiButton = findViewById(R.id.category_button_sushi);

        riceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent riceIntent = new Intent(MainActivity.this, RiceActivity.class);
                MainActivity.this.startActivity(riceIntent);
            }
        });

        noodleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noodleIntent = new Intent(MainActivity.this, NoodleActivity.class);
                MainActivity.this.startActivity(noodleIntent);
            }
        });

        chickenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chickenIntent = new Intent(MainActivity.this, ChickenActivity.class);
                MainActivity.this.startActivity(chickenIntent);
            }
        });

        meatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meatIntent = new Intent(MainActivity.this, MeatActivity.class);
                MainActivity.this.startActivity(meatIntent);
            }
        });

        desertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent desertIntent = new Intent(MainActivity.this, DesertActivity.class);
                MainActivity.this.startActivity(desertIntent);
            }
        });

        sushiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sushiIntent = new Intent(MainActivity.this, SushiActivity.class);
                MainActivity.this.startActivity(sushiIntent);
            }
        });



        //하단 메뉴바
        final Button homeButton = (Button)findViewById(R.id.homeButton);
        final Button pickButton = (Button) findViewById(R.id.pickButton);
        final Button myButton = (Button)findViewById(R.id.myButton);
        final Button logoutButton = (Button) findViewById(R.id.loginButton);

        homeButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent homeIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(homeIntent);

            }
        });
/*
        pickButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent pickIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(pickIntent);

            }
        });*/

        myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MainActivity.this, CustomerMyMenuActivity.class);
                MainActivity.this.startActivity(myIntent);

            }
        });
/*
        logoutButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent logoutIntent = new Intent(MainActivity.this, MainActivity.class);
                MainActivity.this.startActivity(logoutIntent);

            }
        });*/
    }

}
