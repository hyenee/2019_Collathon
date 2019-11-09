package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.collathon.jamukja.store.category.list.StoreListActivity;
import com.collathon.jamukja.user_info.customer.CustomerMyMenuActivity;
import com.collathon.janolja.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //카테고리 버튼 클릭하면 이동, 순서대로 버튼 1~6
        ImageButton rice = findViewById(R.id.category_button_rice); //한식
        ImageButton noodle = findViewById(R.id.category_button_noodle); //국수
        ImageButton chicken = findViewById(R.id.category_button_chicken); //치킨
        ImageButton meat = findViewById(R.id.category_button_meat); //고기
        ImageButton desert = findViewById(R.id.category_button_desert); //디저트
        ImageButton sushi = findViewById(R.id.category_button_sushi); //스시

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent riceIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(riceIntent);
            }
        });

        noodle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent noodleIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(noodleIntent);
            }
        });

        chicken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent chickenIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(chickenIntent);
            }
        });

        meat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meatIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(meatIntent);
            }
        });

        desert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent desertIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(desertIntent);
            }
        });

        sushi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sushiIntent = new Intent(MainActivity.this, StoreListActivity.class);
                MainActivity.this.startActivity(sushiIntent);
            }
        });

        //하단 메뉴바
        final Button homeButton = (Button)findViewById(R.id.homeButton); //홈
        final Button pickButton = (Button) findViewById(R.id.pickButton); //찜 목록
        final Button myButton = (Button)findViewById(R.id.myButton); //내 정보
        final Button logoutButton = (Button) findViewById(R.id.loginButton); //로그아웃

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
