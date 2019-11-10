package com.collathon.jamukja.customer.store.category.detail;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.NetworkManager;
import com.collathon.janolja.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class StoreDetailListActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail_menu_recycler);
        init();
        getData();
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.store_menu_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> menuName = Arrays.asList("국밥", "곱창", "김치찌개");
        List<String> menuDescription = Arrays.asList(
                "돼지국밥, 순대국밥",
                "야채곱창, 순대곱창",
                "김치찌개전문점"
        );
        List<Integer> menuPrice = Arrays.asList(10000, 20000, 15000);
        Log.v("tag", "data 저장");

        for (int i = 0; i < menuName.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setName(menuName.get(i));
            data.setPrice(menuPrice.get(i));
            data.setDescription(menuDescription.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
            Log.v("tag", "adapter에 추가");
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
