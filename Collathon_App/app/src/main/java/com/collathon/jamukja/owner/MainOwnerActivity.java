package com.collathon.jamukja.owner;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.owner.BlackList.Owner_BlackList;
import com.collathon.janolja.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainOwnerActivity extends AppCompatActivity {
    private RecyclerAdapter adapter;
    private TextView textView_ownerName;
    private static final String TAG = "MainActivity";
    private Button btn_home,btn_black,btn_book;
    String ownerID;
    private AlertDialog.Builder builder;
    String ownerShop;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_main);
        textView_ownerName = findViewById(R.id.user_name);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("Owner_id");
        ownerID = name;
        textView_ownerName.setText(name);
        init();
        getData();


        //각 버튼 클릭시 해당하는 페이지로 넘어가게 하는 코드
        btn_black = findViewById(R.id.blackButton);
        btn_book = findViewById(R.id.bookButton);
        btn_black.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainOwnerActivity.this, Owner_BlackList.class);
                startActivity(intent);
            }
        });
    }

    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recycler_store);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new RecyclerAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.

        //제이슨 파싱 코드
        //카테고리에 따른 가게 출력

        final List<String> shop_name_list = new ArrayList<>();
        final List<String> shop_id_list = new ArrayList<>();

        //서버 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/ownShop";
                        site += "?id="+ownerID;
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
                                Log.i("STORE", String.valueOf(jsonArray.length()));
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String shop_id = jsonObject.getString("id");
                                    String shop_name = jsonObject.getString("name");

                                    Log.i("STORE",shop_name);
                                    shop_name_list.add(shop_name);
                                    shop_id_list.add(shop_id);
                                }
                                for (int i = 0; i < shop_name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    Data data = new Data();
                                    data.setTitle(shop_name_list.get(i));
                                    data.setOwner(ownerID);
                                    data.setId(shop_id_list.get(i));
                                    // 각 값이 들어간 data를 adapter에 추가합니다.
                                    adapter.addItem(data);
                                }
                                adapter.notifyDataSetChanged();
                                // adapter의 값이 변경되었다는 것을 알려줍니다.

                            }
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

            });
            Log.i("STORE", "여기까지4");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("STORE", "여기까지5");



    }

}