package com.collathon.jamukja.customer.reservation.confirm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

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

public class ReservationConfirmActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    Handler handler;
    private String userID;
    List<Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_confirm_recycler);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        handler = new Handler();

        init();
        getData();

    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.confirm_recycler);

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
        list = new ArrayList<>();
        //서버 디비 값 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/reservation/detail";
                        site += "?id="+userID;
                        Log.i("TICKET CONFIRM", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i("TICKET CONFIRM", "서버 연결됨");
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
                                Log.i("TICKET CONFIRM, ", "서버: " + rec_data);

                                //list.add(new Data("0", "0", "0", "0"));

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String reservation_id = jsonObject.getString("reservation_id");
                                    String shop = jsonObject.getString("shop");
                                    String menu = jsonObject.getString("menu");
                                    String count = jsonObject.getString("count");
                                    String time = jsonObject.getString("time");
                                    String temp = "";
                                    temp += "  "+ menu  + "  x " + count + "개";
                                    list.add(new Data(reservation_id, shop, temp, time));
                                }

                                for (int index = 0; index < list.size(); index++) {
                                    for(int j = 0; j < index; j++){
                                        if (list.get(index).getId()== list.get(j).getId()){
                                            String tt = list.get(index).getMenu();
                                            tt += "\n"+ list.get(j).getMenu();
                                            list.get(index).setMenu(tt);
                                            list.set(j, new Data("-1", "-1", "-1", "-1"));
                                        }
                                    }
                                }

                                for (int index = list.size()-1; index >= 0; index--) {
                                    if(list.get(index).getId().equals("-1")
                                            || list.get(index).getMenu().equals(null))
                                        list.remove(index);
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = list.get(i);
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

                }
            });

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}