package com.collathon.jamukja.owner.Seat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.owner.Data;
import com.collathon.jamukja.owner.RecyclerAdapter;
import com.collathon.jamukja.owner.Store_info;
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

public class Owner_Seat_Info extends AppCompatActivity {
    String shopID;
    FirstManagerAdapter adapter;
    TextView textView ;
    Bundle bundle;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_seat_info);
        final Intent intent = getIntent();
        shopID = intent.getExtras().getString("shop_id");

        textView = findViewById(R.id.et_shopID);
        textView.setText(shopID);

        setRecyclerView();
        getData();

        recyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Seat_Info.this);
                View view = LayoutInflater.from(Owner_Seat_Info.this).inflate(R.layout.owner_reservation_popup, null, false);
                builder.setView(view);




                final AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    private void setRecyclerView() {
        recyclerView = findViewById(R.id.recycler_table);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);


        adapter = new FirstManagerAdapter();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getData() {
        // 임의의 데이터입니다.
        final List<String> book_id = new ArrayList<>();
        final List<String> book_time = new ArrayList<>();
        final List<String> book_table = new ArrayList<>();
        final List<Integer> book_count = new ArrayList<>();

        //서버 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/reservation/table/owner";
                        site += "?shop=" + shopID;
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

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    String book_userID = jsonObject.getString("user");
                                    String book_userTime = jsonObject.getString("time");
                                    String book_userTable = jsonObject.getString("number");
                                    int book_userCount = jsonObject.getInt("count");

                                    if(book_id.size() == 0){
                                        book_id.add(book_userID);
                                        book_time.add(book_userTime);
                                        book_table.add(book_userTable);
                                        book_count.add(book_userCount);
                                    }
                                    if(book_id.contains(book_userID)==false){
                                        book_id.add(book_userID);
                                        book_time.add(book_userTime);
                                        book_table.add(book_userTable);
                                        book_count.add(book_userCount);
                                    }
                                    else{
                                        BookData data_over = new BookData();
                                        data_over.setName(book_userID);
                                        data_over.setTable(book_userTable);
                                        data_over.setCount(book_userCount);
                                        data_over.setTime(book_userTime);
                                        adapter.addOverItem(data_over);

                                    }

                                }
                                for (int i = 0; i < book_id.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    BookData data = new BookData();
                                    data.setName(book_id.get(i));
                                    data.setTime(book_time.get(i));
                                    data.setCount(book_count.get(i));
                                    data.setTable(book_table.get(i));
                                    // 각 값이 들어간 data를 adapter에 추가합니다.
                                    String temp = data.getName();
                                    Log.i("STORE","중복 데이터 거르깃"+temp);

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
}
