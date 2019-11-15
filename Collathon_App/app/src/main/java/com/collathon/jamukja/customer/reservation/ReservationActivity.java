package com.collathon.jamukja.customer.reservation;


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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ReservationActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    Handler handler;
    Button decisionMenuButton, timeButton, reserveButton; //메뉴 확정, 시간, 예약하기 버튼
    EditText number_table_1, number_table_2, number_table_4; //메뉴 수량, 각 테이블 별 수량 체크
    TextView time_id; //시간 표시할 TextView,
    String reservation_time="0"; //예약시간 초기 0으로 설정
    int selected = 0; //예약 시간 선택 다이얼로그에 쓸 변수
    private String client_id, shop_id; //사용자 id, 가게 id 받아옴
    String current; //현재 시간 받아오는 변수

    List<String> name_list, price_list, count_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_recycler);

        //사용자, 가게 id 받아옴
        Intent intent = getIntent();
        client_id = intent.getExtras().getString("userID");
        shop_id = intent.getExtras().getString("shopID");

        number_table_1 = (EditText) findViewById(R.id.number_table_1);
        number_table_2 = (EditText) findViewById(R.id.number_table_2);
        number_table_4 = (EditText) findViewById(R.id.number_table_4);
        time_id = (TextView)findViewById(R.id.time_id); //예약 선택한 시간 text로 보여줌

        //decisionMenuButton = (Button)findViewById(R.id.decisionMenuButton);
        timeButton = (Button)findViewById(R.id.timeButton);
        reserveButton = (Button)findViewById(R.id.reserveButton);

        handler = new Handler();

        init();
        getData();

        //시간 버튼 클릭하면 예약 시간 선택 가능
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTime();
            }
        });

        /*
        current = currentTime();
        //메뉴 확정 버튼 클릭
        decisionMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReservationMenu();
            }
        });


         */
        //예약하기 버튼 누르면 예약 정보 전송
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                current = currentTime();
                addReservation();
                addReservationMenu();
                addReservationTable();
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

    //메뉴, 가격 출력
    private void getData() {
        name_list = new ArrayList<>();
        price_list = new ArrayList<>();
        count_list = new ArrayList<>();
        TimeZone time;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH");
        time = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(time);
        Log.i("MENU", "CURRENT TIME : "+ df.format(date));
        final String HH = df.format(date);
        Log.i("MENU", HH);

        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/categories/menu";
                        site += "?id="+shop_id+"&time="+HH;
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
                                    String count = jsonObject.getString("count");
                                    name_list.add(name);
                                    price_list.add(price);
                                    count_list.add(count);
                                    Log.i("RESERVATION", "추출 결과 :  " + name+", "+price+"," + count);
                                }
                                for(int i=0; i<name_list.size(); i++){
                                    Log.i("RESERVATION(get data)", "리스트 값 :  " + name_list.get(i)+", "+price_list.get(i)+", " + count_list.get(i));
                                }
                                for (int i = 0; i < name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = new Data();
                                    data.setName(name_list.get(i));
                                    data.setPrice(price_list.get(i));
                                    data.setCount(count_list.get(i));

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

    //예약 시간 선택
    private void selectTime(){
        final String[] time = {"10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
                "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00"};
        final int[] selectedIndex = {0};

        AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
        builder.setTitle("예약 시간 선택")
                .setSingleChoiceItems(time, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedIndex[0] = which;
                        Log.i("RESERVATION(selectTime)", "which : "+which+", selectedIndex[0] : " +selectedIndex[0]);
                    }
                })
                .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(ReservationActivity.this, time[selectedIndex[0]],
                                Toast.LENGTH_SHORT).show();
                        reservation_time = time[selectedIndex[0]];
                        Log.i("RESERVATION(selectTime)", "RESERVATION TIME : " + reservation_time);
                        selected = selectedIndex[0];
                        time_id.setText(reservation_time); //time_id 화면에 보내줌
                        getReservationTable(reservation_time);
                    }
                }).create().show();
        //reservation_time = time[selectedIndex[0]];
        Log.i("RESERVATION(selectTime)", "RESERVATION TIME : " + reservation_time);
    }

    //현재 시간, 사용자id, 예약 시간, 가게 id 서버로 전송
    private void addReservation(){
        try {
            NetworkManager nm = new NetworkManager();
                String client_site = "/reservation/add?current="+current+"&user="+client_id
                        +"&time="+reservation_time+"&shop="+shop_id;
                Log.i("RESERVATION(addReservation)", "SITE= "+client_site);
                nm.postInfo(client_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("RESERVATION(addReservation)", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("RESERVATION(addReservation)", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("예약 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                /*
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("예약 성공")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //메뉴, 수량 서버로 전송
    private void addReservationMenu(){
        //adapter = new RecyclerAdapter();
        List<String> menu = adapter.menu_list;
        List<String> count = adapter.count_list;
        for(int i=0; i<menu.size(); i++){
            Log.i("RESERVATION(addReservationMenu)", "menu, count" + menu.get(i)+", "+count.get(i));
            if(!count.get(i).equals("0"))
                addReservationMenuPost(menu.get(i), count.get(i));
        }
    }

    private void addReservationMenuPost(String menu_name, String menu_count){
        try {
            NetworkManager nm = new NetworkManager();
            String client_site = "/reservation/add/menu?current="+current+"&user="+client_id
                    +"&shop="+shop_id+"&menu="+menu_name+"&count="+menu_count;
            Log.i("RESERVATION(addReservationMenu)", "SITE= "+client_site);
            nm.postInfo(client_site, "POST");

            while(true){ // thread 작업이 끝날 때까지 대기
                if(nm.isEnd){
                    break;
                }
                Log.i("RESERVATION(addReservationMenu)", "아직 작업 안끝남.");
            }
            JSONObject jsonObject = nm.getResult();
            String success = jsonObject.getString("result");
            Log.i("RESERVATION(addReservationMenu)", "서버에서 받아온 result = " + success);

            if (success.equals("ERROR")){
                AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                builder.setMessage("예약 실패")
                        .setNegativeButton("다시 시도", null)
                        .create()
                        .show();
            }
                /*
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ReservationActivity.this);
                    builder.setMessage("예약 성공")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }*/


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //테이블 예약
    public void addReservationTable(){
        String table_1 = number_table_1.getText().toString();
        String table_2 = number_table_2.getText().toString();
        String table_4 = number_table_4.getText().toString();

        //각 table 마다 빈칸인 경우 0으로 초기화
        if(table_1.getBytes().length<=0){
            table_1="0";
        }
        else{
            addReservationTablePost(1, Integer.parseInt(table_1));

        }
        if(table_2.getBytes().length<=0){
            table_2="0";
        }
        //if(Integer.parseInt(table_2)>0)
        else{
            addReservationTablePost(2, Integer.parseInt(table_2));

        }
        if(table_4.getBytes().length<=0){
            table_4="0";
        }
        else{
            addReservationTablePost(4, Integer.parseInt(table_4));
        }
        Log.i("RESERVATION(addReservationTable)", "table_1 : " +table_1+", table_2 : "+ table_2+", table_4 : " + table_4);
    }

    //현재 시간, 사용자id, 가게id, 예약한 테이블 종류 및 인원 서버로 전송
    public void addReservationTablePost(int number_of_table, int table_count){
        //테이블 종류에 따라 post
        try {
            NetworkManager nm = new NetworkManager();
            String client_site = "/reservation/add/table?current="+current+"&user="+client_id+"&shop="+shop_id
                    +"&table="+number_of_table+"&count="+table_count;
            Log.i("RESERVATIONRESERVATION(addReservationTable)", "SITE= "+client_site);
            nm.postInfo(client_site, "POST");

            while(true){ // thread 작업이 끝날 때까지 대기
                if(nm.isEnd){
                    break;
                }
                Log.i("RESERVATIONRESERVATION(addReservationTable)", "아직 작업 안끝남.");
            }
            JSONObject jsonObject = nm.getResult();
            String success = jsonObject.getString("result");
            Log.i("RESERVATIONRESERVATION(addReservationTable)", "서버에서 받아온 result = " + success);

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
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    //시간에 따라 테이블 정보 값 서버에서 가져옴
    public void getReservationTable(String time){
        reservation_time = time;
        final List<String> id_list, number_list, remain_table_list; //파싱해온 값 저장하는 리스트
        id_list = new ArrayList<>();
        number_list = new ArrayList<>();
        remain_table_list = new ArrayList<>();
        final TextView table_1 = (TextView)findViewById(R.id.table_1);
        final TextView table_2 = (TextView)findViewById(R.id.table_2);
        final TextView table_4 = (TextView)findViewById(R.id.table_4);


        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/reservation/table/remain";
                        site += "?id="+shop_id+"&time="+reservation_time;
                        Log.i("MENU", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i("RESERVATION(getReservationTable)", "서버 연결됨");
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
                                Log.i("RESERVATION(getReservationTable), ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String shop_id = jsonObject.getString("shop_id");
                                    String number = jsonObject.getString("number");
                                    String remain_table = jsonObject.getString("remain_table");
                                    id_list.add(shop_id);
                                    number_list.add(number);
                                    remain_table_list.add(remain_table);
                                    Log.i("RESERVATION(getReservationTable)", "추출 결과 :  " + shop_id+", "+number+", " + remain_table);
                                }

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        for(int i=0; i<id_list.size(); i++){
                                            Log.i("RESERVATION(getReservationTable)", "리스트 값 :  " + id_list.get(i)+", "+number_list.get(i)+", " + remain_table_list.get(i));

                                            if (number_list.get(i).equals("1"))
                                                table_1.setText(remain_table_list.get(i));
                                            else if(number_list.get(i).equals("2"))
                                                table_2.setText(remain_table_list.get(i));
                                            else if(number_list.get(i).equals("4"))
                                                table_4.setText(remain_table_list.get(i));
                                            else{

                                            }
                                        }

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

    public String currentTime(){
        //현재 시간 가져오기
        TimeZone time;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyMMddHHmmss");
        time = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(time);
        Log.i("RESERVATION", "CURRENT TIME : "+ df.format(date));
        String current = df.format(date);

        return current;
    }

    private void startToast(String msg) { Toast.makeText(this, msg, Toast.LENGTH_SHORT).show(); }
    }