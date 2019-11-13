package com.collathon.jamukja.customer.reservation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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

public class ReservationActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    //private String menuname;
    Handler handler;
    Button calButton, timeButton, reserveButton;
    TextView textView;
    int choiceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_recycler);

        calButton = (Button)findViewById(R.id.calculationButton);
        timeButton = (Button)findViewById(R.id.timeButton);
        reserveButton = (Button)findViewById(R.id.reserveButton);

        handler = new Handler();
        //Intent intent = getIntent();
        //String name = intent.getExtras().getString("shopname");
        //menuname="1";
        init();
        getData();

        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSingleChoiceDialog();
            }
        });

    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.reservation_recycler);

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
        final List<String> name_list = new ArrayList<>();
        final List<String> price_list = new ArrayList<>();

        //서버 디비 값 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/categories/menu";
                        site += "?id=1";
                        Log.i("MENU", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i("RESERVATION", "서버 연결됨");
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
                                Log.i("RESERVATION, ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String price = jsonObject.getString("price");
                                    name_list.add(name);
                                    price_list.add(price);
                                    Log.i("RESERVATION", "추출 결과 :  " + name+", "+price);
                                }
                                for(int i=0; i<name_list.size(); i++){
                                    Log.i("RESERVATION", "리스트 값 :  " + name_list.get(i)+", "+price_list.get(i));
                                }
                                for (int i = 0; i < name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = new Data();
                                    data.setName(name_list.get(i));
                                    data.setPrice(price_list.get(i));

                                    // 각 값이 들어간 data를 adapter에 추가합니다.
                                    adapter.addItem(data);
                                }
                                //adapter.notifyDataSetChanged();

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

    private void showSingleChoiceDialog(){
        final int backup = choiceIndex; //이전 선택 상태 값 백업하는 변수
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("예약 시간 선택");

        final String[] time = {"10:00", "11:00", "12:00", "13:00", "14:00", "15:00", "16:00",
                "17:00", "18:00", "19:00", "20:00", "21:00"};

        builder.setSingleChoiceItems(time, choiceIndex, new DialogInterface.OnClickListener() {
            //버튼의 선택 상태가 변경된 경우 처리하는 위치
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //which값이 선택 항목의 index
                choiceIndex = which;
                Toast.makeText(ReservationActivity.this, time[which], Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                textView.setText(time[choiceIndex]);
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                choiceIndex = backup;
            }
        });

        builder.create();
        builder.show();
    }



}
