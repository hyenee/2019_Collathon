package com.collathon.jamukja.store.category.meat_4;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

public class MeatStoreListActivity extends AppCompatActivity {
    private TextView textView_name;
    private TextView textView_flowers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_meat);

        Intent intent = getIntent();
    }
}
