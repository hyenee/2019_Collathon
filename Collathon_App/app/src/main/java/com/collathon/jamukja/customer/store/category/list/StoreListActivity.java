package com.collathon.jamukja.customer.store.category.list;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;

public class StoreListActivity extends AppCompatActivity {
    private static final String TAG = "StoreListActivity";
    private RecyclerAdapter adapter;
   // Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_list_recycler);

        init();

        getData();
    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.store_list_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        //카테고리에 따른 가게 출력
        final List<String> shop_name_list = new ArrayList<>();
        final List<String> menu_name_list = new ArrayList<>();

        //서버 디비 값 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/categories";
                        site += "?category=rice";
                        Log.i("STORE", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i("STORE", "서버 연결됨");
                                // 스트림 추출 : 맨 처음 타입을 버퍼로 읽고 그걸 스트링버퍼로 읽음
                                InputStream is = connection.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                                BufferedReader br = new BufferedReader(isr);
                                String str = null;
                                StringBuffer buf = new StringBuffer();

                                // 읽어온다.
                                do {
                                    str = br.readLine();
                                    if (str != null) {
                                        buf.append(str);
                                    }
                                } while (str != null);
                                br.close(); // 스트림 해제

                                String rec_data = buf.toString();
                                Log.i("STORE, ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String shop_name = jsonObject.getString("shop_name");
                                    String menu_name = jsonObject.getString("menu_name");
                                    shop_name_list.add(shop_name);
                                    menu_name_list.add(menu_name);
                                    Log.i("STORE", "추출 결과 :  " + shop_name+", "+menu_name);
                                }
                                for(int i=0; i<shop_name_list.size(); i++){
                                    Log.i("STORE", "리스트 값 :  " + shop_name_list.get(i)+", "+menu_name_list.get(i));
                                }
                                for (int i = 0; i < shop_name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = new Data();
                                    data.setShop_name(shop_name_list.get(i));
                                    data.setMenu_name(menu_name_list.get(i));

                                    // 각 값이 들어간 data를 adapter에 추가합니다.
                                    adapter.addItem(data);
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        // adapter의 값이 변경되었다는 것을 알려줍니다.
                                        adapter.notifyDataSetChanged();
                                    }
                                });
                            }
                            connection.disconnect(); // 연결 끊기
                        }
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                    Log.i("STORE", "여기까지3");
                }
            });Log.i("STORE", "여기까지4");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("STORE", "여기까지5");
    }

}
