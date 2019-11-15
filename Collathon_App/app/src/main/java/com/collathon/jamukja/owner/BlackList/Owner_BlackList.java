package com.collathon.jamukja.owner.BlackList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.collathon.jamukja.MainActivity;
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

public class Owner_BlackList extends AppCompatActivity {

    private static final String TAG = "Owner_BlackList";
    private BlackAdapter adapter;
    private String ownerID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_black_list);

        setRecyclerView();
        getBlackListData();
    }

    private void setRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.black_list_recycler);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new BlackAdapter();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });
        recyclerView.setAdapter(adapter);
    }

    //http://oreh.onyah.net:7080/blacklist/add?id=client_id&shop=shopid&comment=comment 등록
    //http://oreh.onyah.net:7080/blacklist/all

    private void getBlackListData() {
        //카테고리에 따른 가게 출력
        final List<String> client_id_list = new ArrayList<>();
        final List<Integer> count_list = new ArrayList<>();

        //서버 디비 값 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/blacklist/all";
                        Log.i("STORE", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i(TAG, "서버 연결됨");
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
                                Log.i(TAG, "서버 get data: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String client_id = jsonObject.getString("client_id");
                                    int count = jsonObject.getInt("count");

                                    client_id_list.add(client_id);
                                    count_list.add(count);
                                    Log.i(TAG, "추출 결과 :  " + client_id + ", " + count);
                                }

                                for (int i = 0; i < client_id_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    BlackData data = new BlackData();
                                    data.setID(client_id_list.get(i));
                                    data.setCount(count_list.get(i));

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void startActivity(Class c) {
        Intent intent = new Intent(Owner_BlackList.this, c);
        startActivity(intent);
    }

}