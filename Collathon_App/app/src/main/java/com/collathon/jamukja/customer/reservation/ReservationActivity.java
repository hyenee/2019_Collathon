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
import android.widget.EditText;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ReservationActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    //private String menuname;
    Handler handler;
    Button decisionMenuButton, timeButton, reserveButton;
    TextView textView;
    EditText menu_number; //메뉴 수량 체크
    int choiceIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_recycler);

        menu_number = (EditText) findViewById(R.id.reservation_menu_number);

        decisionMenuButton = (Button)findViewById(R.id.decisionMenuButton);
        timeButton = (Button)findViewById(R.id.timeButton);
        reserveButton = (Button)findViewById(R.id.reserveButton);

        handler = new Handler();

        init();
        getData();



        //시간 버튼 클릭하면 예약 시간 선택 가능
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getTime();
            }
        });

        //예약하기 버튼 누르면 예약 정보 전송
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getReserve();
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

    /*
    private void getOrderList(){
        String menuNumber = menu_number.getText().toString();

        //비밀번호 변경 POST
        int numberlen = menuNumber.length();
        Log.i("RESERVATION", "NUMBER : "+ numberlen+", "+menuNumber);
        try {
            NetworkManager nm = new NetworkManager();
            if (menu_number.length()>0) {
                String client_site = "/reservation/add/menu?id=1"+ "&new=" + newPasswd;
                Log.i("RESERVATION", "SITE= "+client_site);
                nm.postInfo(client_site, "PATCH"); //받은 placeId에 따른 장소 세부 정보

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("RESERVATION", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("RESERVATION", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("메뉴 수량 선택 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("메뉴 수량 선택 성공")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();

                }
            } else {
                startToast("입력하지 않은 정보가 존재합니다.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
/*
    private void getOrderList(){
        final String[] num = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        final int[] selectedIndex = {0};
        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("수량 선택")
                .setSingleChoiceItems(num, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedIndex[0] = which;
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(ReservationActivity.this, num[selectedIndex[0]],
                                Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

 */
    private void getTime(){
        final String[] time = {"10:00~11:00", "11:00~12:00", "12:00~13:00", "13:00~14:00", "14:00~15:00", "15:00~16:00", "16:00~17:00",
                "17:00~:18:00", "18:00~19:00", "19:00~20:00", "20:00~21:00", "21:00~22:00"};
        final int[] selectedIndex = {0};
        final String ReserveTime;

        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("예약 시간 선택")
                .setSingleChoiceItems(time, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedIndex[0] = which;
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(ReservationActivity.this, time[selectedIndex[0]],
                                Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
        ReserveTime = time[selectedIndex[0]];
        Log.i("RESERVATION", "TIME : " + ReserveTime);

    }



    private void getReserve(){
//        String menuNumber = menu_number.getText().toString();
        //       int numberlen = menuNumber.length();

        //현재 시간 가져오기
        long now = System.currentTimeMillis();
        //Data 형식으로 고침
        Date mDate = new Date(now);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYMMDDHHmmss");
        String classification = simpleDateFormat.format(mDate);
        Log.i("RESERVATION", "DATE : "+ classification); //현재 시각 -> 서버로 보냄

/*
        Log.i("RESERVATION", "NUMBER : "+ numberlen+", "+menuNumber);
        try {
            NetworkManager nm = new NetworkManager();
            if (menu_number.length()>0) {
                String client_site = "/reservation/add?current"+current+"&user="+client_id
                        +"&time="+reservation_time+"&shop="+shop_id;
                Log.i("RESERVATION", "SITE= "+client_site);
                nm.postInfo(client_site, "PATCH"); //받은 placeId에 따른 장소 세부 정보

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("RESERVATION", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("RESERVATION", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("예약 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("예약 성공")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();

                }
            } else {
                startToast("입력하지 않은 정보가 존재합니다.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

 */

    }
}
