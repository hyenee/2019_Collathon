package com.collathon.jamukja.owner.confirm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.customer.user_info.customer.CustomerMyMenuActivity;
import com.collathon.jamukja.owner.Seat.Owner_Reservation_seat;
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

public class Owner_Reservation_View extends AppCompatActivity {
    private RecyclerAdapter adapter;
    Handler handler;
    private String ownerID,shopID;
    List<String> tableList;

    List<String> reservation_id_list, shop_list, menu_list, count_list, time_list;
    List<Data> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_reservation_view);

        Intent intent = getIntent();
        //ownerID = intent.getExtras().getString("owner_id");
        shopID = intent.getExtras().getString("shopID");
        ownerID = intent.getExtras().getString("ownerID");

        handler = new Handler();

        init();
        getUserReservationTable();
        getData();


    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.owner_recycler_book);

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
                        String site = NetworkManager.url + "/reservation/owner";
                        site += "?shop="+shopID;
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
                                Log.i("STORE, ", "서버: " + rec_data);

                                //list.add(new Data("0", "0", "0", "0"));

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String reservation_id = jsonObject.getString("reservation_id");
                                    String user = jsonObject.getString("user");
                                    String menu = jsonObject.getString("menu");
                                    String count = jsonObject.getString("count");
                                    String time = jsonObject.getString("time");
                                    String temp = "";
                                    //temp += "  "+ menu  + "  x " + count + "개";
                                    String tabletemp = "";

                                    temp += "  "+ menu  + "  x " + count + "개";

                                    for(int j=0; j<tableList.size(); j++){
                                        if(tableList.get(j).equals(reservation_id)){
                                            tabletemp += tableList.get(j+1);
                                            Log.i("CONFIRM", "table temp : "+ tabletemp);
                                        }
                                    }

                                    list.add(new Data(user, temp, time, tabletemp, reservation_id, "0"));

//                                    list.add(new Data(reservation_id, user, temp, time));

                                }

                                for (int index = 0; index < list.size(); index++) {
                                    Log.i("예약번호", list.get(index).getId());
                                    for(int j = 0; j < index; j++){
                                        if (list.get(index).getReservation().equals(list.get(j).getReservation()) ){
                                            String tt = list.get(index).getShop();
                                            tt += "\n"+ list.get(j).getShop();
                                            list.get(index).setShop(tt);

//                                            list.set(index, new Data(list.get(index).getId(),
//                                                    list.get(index).getShop(), , list.get(index).getTime()));
                                            list.set(j, new Data("-1", "-1", "-1", "-1","-1","-1"));
                                        }
                                    }
                                }

                                for (int index = list.size()-1; index >= 0; index--) {
                                    if(list.get(index).getId().equals("-1")
                                            || list.get(index).getShop().equals(null))
                                        list.remove(index);
                                }

                                for (int i = 0; i < list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = list.get(i);
                                    // 각 값이 들어간 data를 adapter에 추가합니다.
                                    adapter.addItem(data);
                                }
                                adapter.shopID = shopID;
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
    public void getUserReservationTable(){
        tableList = new ArrayList<>();
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/reservation/table/owner";
                        site += "?shop="+shopID;
                        Log.i("CONFIRM", site);

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
                                Log.i("CONFIRM, ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String reservation_id = jsonObject.getString("reservation_id");
                                    // String shop = jsonObject.getString("shop");
                                    String number = jsonObject.getString("number");
                                    String count = jsonObject.getString("count");
                                    String time = jsonObject.getString("time");
                                    String temp = "";
                                    temp += " "+ number  + "인 테이블  x " + count + "개  ";

                                    tableList.add(reservation_id);
                                    tableList.add(temp);
                                    Log.i("CONFIRM", tableList.toString());
                                }

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

    public void onBackPressed() {
        Intent intent = new Intent(this, Owner_Reservation_seat.class);
        intent.putExtra("ownerID",ownerID);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }


}
