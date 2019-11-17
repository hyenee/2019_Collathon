package com.collathon.jamukja.customer.reservation.ticket_confirm;

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

public class ReservationTicketConfirmActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    Handler handler;
    private String userID;

    List<String> reservation_id_list, shop_list, menu_list, count_list, time_list;
    List<OrderList> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_ticket_confirm_recycler);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        handler = new Handler();

        init();
        getData();

    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.ticket_confirm_recycler);

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
        //예약 id, 예약한 가게, 메뉴, 수량, 시간 출력
        reservation_id_list = new ArrayList<>();
        shop_list = new ArrayList<>();
        menu_list = new ArrayList<>();
        count_list = new ArrayList<>();
        time_list = new ArrayList<>();
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

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String reservation_id = jsonObject.getString("reservation_id");
                                    String shop = jsonObject.getString("shop");
                                    String menu = jsonObject.getString("menu");
                                    String count = jsonObject.getString("count");
                                    String time = jsonObject.getString("time");
                                    reservation_id_list.add(reservation_id);
                                    shop_list.add(shop);
                                    menu_list.add(menu);
                                    count_list.add(count);
                                    time_list.add(time);
                                    Log.i("TICKET CONFIRM", "추출 결과 :  " + reservation_id+", "+ shop+", "+menu+"," + count+", "+ time);
                                }
                                for(int i=0; i<shop_list.size(); i++){
                                    list.add(new OrderList(reservation_id_list.get(i), menu_list.get(i), count_list.get(i)));
                                    Log.i("TICKET CONFIRM", "리스트 값 :  " + reservation_id_list.get(i)+", "+ shop_list.get(i)+", "+menu_list.get(i)+", " + count_list.get(i)+", " +time_list.get(i));
                                    Log.i("TICKET CONFIRM", "list : " + reservation_id_list.get(i)+", "+ menu_list.get(i)+", "+ count_list.get(i));
                                }

                                List<OrderList> sublist = new ArrayList<>();

                                for(int i=0; i<reservation_id_list.size()-1; i++){
                                    if(!list.get(i).order_id.equals(list.get(i+1).order_id)){
                                        //splitList.add((OrderList) list.subList(0, i+1));
                                        sublist = list.subList(0, i+1);
                                       // Data data = new Data();
                                       // data.setOrderList((OrderList) sublist);

                                        //list.remove(list.get(i));
                                        //i=0;
                                    }
                                }
                                for(int i=0; i<sublist.size(); i++){
                                    Log.i("TICKET CONFIRM", "split list : " + sublist.get(i).order_menu+", " + sublist.get(i).order_count);
                                }

                                for (int i = 0; i < shop_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = new Data();
                                    data.setId(reservation_id_list.get(i));
                                    data.setShop(shop_list.get(i));
                                    //data.setMenu(menu_list.get(i));
                                    //data.setCount(count_list.get(i));
                                    data.setTime(time_list.get(i));

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
