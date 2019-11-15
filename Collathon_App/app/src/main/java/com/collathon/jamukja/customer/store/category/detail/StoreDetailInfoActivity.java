package com.collathon.jamukja.customer.store.category.detail;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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

public class StoreDetailInfoActivity extends AppCompatActivity {
<<<<<<< HEAD
    private static final String TAG = "StoreDetailInfoActivity";
    private String shopID;
=======
    private String shop_id; //가게 id 받아옴
>>>>>>> d17449e207e10e4b065c4c0f291d032f390b55da

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail_info);

        Intent intent = getIntent();
        shopID = intent.getExtras().getString("shopID");

        final TextView tx_name = findViewById(R.id.store_name);
        final TextView tx_category = findViewById(R.id.store_category);
        final TextView tx_tel = findViewById(R.id.store_phone);
        final TextView tx_address = findViewById(R.id.store_location);

        //사용자, 가게 id 받아옴
        Intent intent = getIntent();
        shop_id = intent.getExtras().getString("shopID");

        //서버 디비 값 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/categories/shop";
<<<<<<< HEAD
                        site += "?id="+shopID;
=======
                        site += "?id="+shop_id;
>>>>>>> d17449e207e10e4b065c4c0f291d032f390b55da
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
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                final String name = jsonObject.getString("name");
                                final String tel = jsonObject.getString("tel");
                                final String address = jsonObject.getString("address");
                                final String category = jsonObject.getString("category");
                                Log.i("STORE", "추출 결과 :  " + name+", "+tel+", "+address+", "+category);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        tx_name.setText(name);
                                        tx_category.setText(category);
                                        tx_tel.setText(tel);
                                        tx_address.setText(address);
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
