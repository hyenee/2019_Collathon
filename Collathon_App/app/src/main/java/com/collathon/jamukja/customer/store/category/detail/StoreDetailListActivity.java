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
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.customer.reservation.ReservationActivity;
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
    private boolean userCheck;
    CheckBox checkBox;
    Handler handler;
    List<String> shop_id_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail_menu_recycler);

        Intent intent = getIntent();
        userID = intent.getExtras().getString("userID");
        shopName = intent.getExtras().getString("shopName");
        shopID = intent.getExtras().getString("shopID");

        findViewById(R.id.menuButton).setOnClickListener(onClickListener); //상세 메뉴
        findViewById(R.id.store_informationButton).setOnClickListener(onClickListener); //가게 정보
        findViewById(R.id.reservationButton).setOnClickListener(onClickListener); //예약하기
        final TextView shopNameTextView = (TextView) findViewById(R.id.store_name); //사용자 이름


        checkBox = (CheckBox) findViewById(R.id.heartCheckBox);
        checkBox.setOnClickListener(onClickListener);
        handler = new Handler();

        shopNameTextView.setText(shopName);
        setRecyclerView();
        getData();
        getLikeShopList();

        checkBox.setOnClickListener(new CheckBox.OnClickListener() {
            @Override
                public void onClick(View v) {
                    if (((CheckBox)v).isChecked()) {
                        Log.i(TAG, "등록중입니다.");
                        postLikeShop();
                        checkBox.setChecked(true);
                    } else {
                        Log.i(TAG, "삭제중입니다.");
                        deleteLikeShop();
                        checkBox.setChecked(false);
                    }
            }
        }) ;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.menuButton:
                    Intent menuIntent = new Intent(StoreDetailListActivity.this, StoreDetailListActivity.class);
                    StoreDetailListActivity.this.startActivity(menuIntent);
                    break;
                case R.id.store_informationButton:
                    Intent infoIntent = new Intent(StoreDetailListActivity.this, StoreDetailInfoActivity.class);
                    infoIntent.putExtra("shopID", shopID);
                    StoreDetailListActivity.this.startActivity(infoIntent);
                    break;
                case R.id.reservationButton:
                    Intent reservIntent = new Intent(StoreDetailListActivity.this, ReservationActivity.class);
                    reservIntent.putExtra("userID", userID);
                    reservIntent.putExtra("shopID", shopID);
                    StoreDetailListActivity.this.startActivity(reservIntent);
                    break;
            }
        }
    };

    private void setRecyclerView() {
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
                        site += "?id="+shopID;
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
                                    Data data = new Data();
                                    data.setName(name_list.get(i));
                                    data.setPrice(price_list.get(i));
                                    data.setDescription((description_list.get(i)));

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

    private void getLikeShopList() {
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/like";
                        site += "?id="+userID;
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
                                    String get_shop_id = jsonObject.getString("shop_id");
                                    shop_id_list.add(get_shop_id);
                                }
                                for(int i=0; i<shop_id_list.size(); i++){
                                    String get_shop_id = shop_id_list.get(i);
                                    if (get_shop_id.equals(shopID)){
                                        Log.i(TAG, "현재 가게 추출 결과 :  " + shopID);
                                        Log.i(TAG, "등록 가게 추출 결과 :  " + get_shop_id);
                                        userCheck = true;
                                        break;
                                    }
                                }
                            }
                            connection.disconnect(); // 연결 끊기
                            checkBox.setChecked(userCheck);
                            Log.i(TAG, "현재 찜가게 등록 여부 :  " + userCheck);
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


    private void postLikeShop() {
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

                if (success.equals("OK")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailListActivity.this);
                    builder.setMessage("꽃게 찜목록에 등록되었습니다.")
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

    private void deleteLikeShop() {
        try {
            NetworkManager nm = new NetworkManager();
            if (userID.length() > 0 && shopID.length() > 0) {
                String site = "/like/delete?shop=" + shopID + "&user=" + userID;
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

                if (success.equals("OK")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(StoreDetailListActivity.this);
                    builder.setMessage("꽃게 찜목록에서 해제되었습니다. 안녕ㅠ")
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
