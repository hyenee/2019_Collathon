package com.collathon.jamukja;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.collathon.jamukja.customer.store.category.list.StoreListActivity;
import com.collathon.jamukja.customer.user_info.customer.CustomerMyMenuActivity;
import com.collathon.janolja.R;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //카테고리 버튼 클릭하면 이동, 순서대로 버튼 1~6
        final ImageButton rice = findViewById(R.id.category_button_rice); //한식
        ImageButton noodle = findViewById(R.id.category_button_noodle); //국수
        ImageButton chicken = findViewById(R.id.category_button_chicken); //치킨
        ImageButton meat = findViewById(R.id.category_button_meat); //고기
        ImageButton desert = findViewById(R.id.category_button_desert); //디저트
        ImageButton sushi = findViewById(R.id.category_button_sushi); //스시

        //이미지 버튼 클릭하면 각 카테고리로 이동, 카테고리명 전달
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
        final Button logoutButton = (Button) findViewById(R.id.logoutButton); //로그아웃

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

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(MainActivity.this, LoginCustomerActivity.class);
                startActivity(intent);
                SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = auto.edit();
                //editor.clear()는 auto에 들어있는 모든 정보를 기기에서 지웁니다.
                editor.clear();
                editor.commit();
                Toast.makeText(MainActivity.this, "로그아웃 하였습니다", Toast.LENGTH_SHORT).show();
                finish();

            }
        });
    }

}
