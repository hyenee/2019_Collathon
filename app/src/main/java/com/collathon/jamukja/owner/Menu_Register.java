package com.collathon.jamukja.owner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

public class Menu_Register extends AppCompatActivity {
    TextView menuTitle,menuContent,menuPrice;
    SecondAdapter adapter;
    Data data;
    Button btn_insert;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_menu_register);
        menuTitle = findViewById(R.id.et_menuTitle);
        menuContent = findViewById(R.id.et_menuContent);
        menuPrice = findViewById(R.id.et_menuPrice);
        setData(menuTitle.getText().toString(),menuContent.getText().toString(),menuPrice.getText().toString());
        btn_insert = findViewById(R.id.btn_insert);

        btn_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Menu_Register.this, Store_info.class);
                startActivity(intent);
            }
        });

    }
    public void setData(String title, String content, String price){
        data = new Data();
        data.setTitle(menuTitle.getText().toString());
        data.setContent(menuContent.getText().toString());
        data.setPrice(menuPrice.getText().toString());

    }
}
