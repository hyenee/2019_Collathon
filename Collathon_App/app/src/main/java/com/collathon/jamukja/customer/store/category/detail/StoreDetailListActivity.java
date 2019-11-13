package com.collathon.jamukja.customer.store.category.detail;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.LoginCustomerActivity;
import com.collathon.jamukja.LoginOwnerActivity;
import com.collathon.jamukja.NetworkManager;
<<<<<<< HEAD
import com.collathon.jamukja.RegisterCustomerActivity;
import com.collathon.jamukja.RegisterOwnerActivity;
=======
import com.collathon.jamukja.customer.reservation.ReservationActivity;
>>>>>>> c053e235af3e8d1c08907da6bae888368a68f4d1
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
import java.util.Arrays;
import java.util.List;

public class StoreDetailListActivity extends AppCompatActivity {
    private static final String TAG = "StoreDetailListActivity";
    private RecyclerAdapter adapter;
    private String userID, shopName, shopID;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail_menu_recycler);
        final Button menuButton = (Button)findViewById(R.id.menuButton); //상세 메뉴
        final Button infoButton = (Button)findViewById(R.id.store_informationButton); //가게 정보
        final Button reservationButton = (Button)findViewById(R.id.reservationButton); //예약하기
        handler = new Handler();

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        shopName = intent.getExtras().getString("shopName");
        shopID = intent.getExtras().getString("shopID");

        init();
        getData();

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent menuIntent = new Intent(StoreDetailListActivity.this, StoreDetailListActivity.class);
                StoreDetailListActivity.this.startActivity(menuIntent);
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent infoIntent = new Intent(StoreDetailListActivity.this, StoreDetailInfoActivity.class);
                StoreDetailListActivity.this.startActivity(infoIntent);
            }
        });

<<<<<<< HEAD
        final CheckBox checkBox = (CheckBox) findViewById(R.id.heartCheckBox);

        // Init
        SharedPreferences settings = getSharedPreferences("mysettings", 0);
        SharedPreferences.Editor editor = settings.edit();
        // Save
        boolean checkBoxValue = checkBox.isChecked();
        editor.putBoolean("heartCheck", checkBoxValue);
        editor.commit();;
        // Load
        checkBox.setChecked(settings.getBoolean("heartCheck", false));

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    checkBox.setChecked(true);
                    likeShop();
                } else {
                    checkBox.setChecked(false);
                }
            }
        }) ;
=======
        reservationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reservIntent = new Intent(StoreDetailListActivity.this, ReservationActivity.class);
                StoreDetailListActivity.this.startActivity(reservIntent);
            }
        });
>>>>>>> c053e235af3e8d1c08907da6bae888368a68f4d1
    }

    private void init() {
        final RecyclerView recyclerView = findViewById(R.id.store_menu_recycler);

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
        final List<String> description_list = new ArrayList<>();

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
                                Log.i("MENU", "서버 연결됨");
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
                                Log.i("MENU, ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String name = jsonObject.getString("name");
                                    String price = jsonObject.getString("price");
                                    String description = jsonObject.getString("description");
                                    name_list.add(name);
                                    price_list.add(price);
                                    description_list.add(description);
                                    Log.i("MENU", "추출 결과 :  " + name+", "+price+", "+description);
                                }
                                for(int i=0; i<name_list.size(); i++){
                                    Log.i("MENU", "리스트 값 :  " + name_list.get(i)+", "+price_list.get(i)+", "+description_list.get(i));
                                }
                                for (int i = 0; i < name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    com.collathon.jamukja.customer.store.category.detail.Data data = new com.collathon.jamukja.customer.store.category.detail.Data();
                                    data.setName(name_list.get(i));
                                    data.setPrice(price_list.get(i));
                                    data.setDescription((description_list.get(i)));

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

    private void likeShop() {
        try {
            NetworkManager nm = new NetworkManager();
            if (userID.length() > 0 && shopID.length() > 0) {
                String site = "/like/add?shop=" + shopID + "&user=" + userID;
                Log.i(TAG, "SITE= "+ site);
                nm.postInfo(site, "POST"); //받은 placeId에 따른 장소 세부 정보

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i(TAG, "아직 작업 안끝남.");
                }

                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i(TAG, "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailListActivity.this);
                    builder.setMessage("찜 목록 등록 실패. 푸하하")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();

                }
                else if (success.equals("OK")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailListActivity.this);
                    builder.setMessage("회원 가입에 성공하셨습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            } else {
                Log.i(TAG,"입력하지 않은 정보가 존재합니다.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent storeListIntent = new Intent();
        storeListIntent.putExtra("userID",userID);
        storeListIntent.putExtra("shopName",shopName);
        storeListIntent.putExtra("shopID",shopID);
        setResult(1234, storeListIntent);
        finish();
    }

}
