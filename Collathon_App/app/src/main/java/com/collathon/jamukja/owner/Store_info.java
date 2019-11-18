package com.collathon.jamukja.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.collathon.jamukja.owner.TimeSale.TimeSaleAdd.TimeSaleActivity;
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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Store_info extends AppCompatActivity {
    private static final String TAG = "Store_info";
    private TextView textView_storename;
    private SecondAdapter adapter;
    private String name, ownerName, shopID, deleteMenu, time;
    String menuName, menuDescription, menuPrice, menuCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_store_info);

        final Intent intent = getIntent();
        name = intent.getExtras().getString("title");
        ownerName = intent.getExtras().getString("owner_ID");
        shopID = intent.getExtras().getString("shop_id");

        textView_storename = findViewById(R.id.et_storename);
        textView_storename.setText(name);

        setRecyclerView();
        getData();

        findViewById(R.id.btn_registerMenu).setOnClickListener(onClickListener);
        findViewById(R.id.btn_remove).setOnClickListener(onClickListener);
        findViewById(R.id.btn_timesaleAdd).setOnClickListener(onClickListener);
        findViewById(R.id.btn_timesaleDelete).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View myview) {
            switch (myview.getId()) {
                case R.id.btn_timesaleAdd:
                    Intent intent = new Intent(Store_info.this, com.collathon.jamukja.owner.TimeSale.TimeSaleAdd.TimeSaleActivity.class);
                    intent.putExtra("shopName", name);
                    intent.putExtra("shopID", shopID);
                    startActivity(intent);
                    break;

                case R.id.btn_timesaleDelete:
                    Intent intent2 = new Intent(Store_info.this, com.collathon.jamukja.owner.TimeSale.TimeSaleDelete.TimeSaleActivity.class);
                    intent2.putExtra("shopName", name);
                    intent2.putExtra("shopID", shopID);
                    startActivity(intent2);
                    break;

                case R.id.btn_registerMenu:
                    AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                    final View rView = LayoutInflater.from(Store_info.this).inflate(R.layout.owner_menu_register, null, false);
                    builder.setView(rView);

                    final Button ButtonSubmit = (Button) rView.findViewById(R.id.btn_insert);
                    final AlertDialog dialog = builder.create();

                    ButtonSubmit.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            // 사용자가 입력한 내용을 가져옴
                            menuName = ((EditText) rView.findViewById(R.id.et_menuTitle)).getText().toString();
                            menuDescription = ((EditText) rView.findViewById(R.id.et_menuContent)).getText().toString();
                            menuPrice = ((EditText) rView.findViewById(R.id.et_menuPrice)).getText().toString();
                            menuCount = ((EditText) rView.findViewById(R.id.et_menuCount)).getText().toString();

                            // ArrayList에 추가
                            MenuData dict = new MenuData();
                            dict.setTitle(menuName);
                            dict.setContent(menuDescription);
                            dict.setPrice(menuPrice);
                            dict.setCount(menuCount);
                            adapter.addItem(dict);

                            addShoMenu();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // adapter의 값이 변경되었다는 것을 알려줍니다.
                                    adapter.notifyDataSetChanged();
                                }
                            });
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                    break;

                case R.id.btn_remove:
                    AlertDialog.Builder rmbuilder = new AlertDialog.Builder(Store_info.this);

                    final String temp[] = new String[adapter.getItemCount()];
                    final int[] index = {0};
                    for (int i = 0; i < temp.length; i++) {
                        temp[i] = adapter.getData(i).getTitle();
                    }

                    rmbuilder.setTitle("삭제할 항목을 고르시오.").setSingleChoiceItems(temp, 0, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int item) {
                            index[0] = item;
                            Toast.makeText(getApplicationContext(), "어디냐 " + temp[item], Toast.LENGTH_SHORT).show();
                            deleteMenu = temp[item];
                        }
                    });
                    rmbuilder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Log.v("가져옴", String.valueOf(index[0]));
                            Toast.makeText(getApplicationContext(), "Phone Model = " + index[0], Toast.LENGTH_SHORT).show();
                            deleteShopMenu(index);
                        }
                    });
                    AlertDialog alert = rmbuilder.create();
                    alert.show();
                    break;
            }
        }
    };

    private void setRecyclerView() {
        final RecyclerView recyclerView = findViewById(R.id.recycler_menu);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SecondAdapter();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                recyclerView.setAdapter(adapter);
            }
        });
    }

    private void getData() {
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        time = currentTime();
                        String site = NetworkManager.url + "/ownMenu";
                        site += "?id=" + shopID + "&time="+time;
                        Log.i(TAG, site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i(TAG, "서버 연결됨");
                                // 스트림 추출 : 맨 처음 타입을 버퍼로 읽고 그걸 스트링버퍼로 읽음
                                InputStream is = connection.getInputStream();
                                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                                BufferedReader br = new BufferedReader(isr);
                                String str = null;
                                StringBuffer buf = new StringBuffer();

                                do {
                                    str = br.readLine();
                                    if (str != null) {
                                        buf.append(str);
                                    }
                                } while (str != null);
                                br.close();
                                String rec_data = buf.toString();

                                JSONArray jsonArray = new JSONArray(rec_data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    MenuData data = new MenuData();
                                    data.setTitle(jsonObject.getString("name"));
                                    data.setPrice(jsonObject.getString("price"));
                                    data.setContent(jsonObject.getString("description"));
                                    data.setCount(jsonObject.getString("count"));

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
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }); //NetworkManager run끝

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public String currentTime(){
        //현재 시간 가져오기
        TimeZone tz;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH");
        tz = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(tz);
        Log.i(TAG, "CURRENT TIME : "+ df.format(date));
        String current = df.format(date);

        return current;
    }


    private void addShoMenu(){
        try {
            NetworkManager nm = new NetworkManager();
            if (menuName.length() > 0 && menuDescription.length() > 0 && menuPrice.length() > 0) {
                String client_site = "/ownMenu/add?id=" + shopID + "&name=" + menuName + "&price="
                        + menuPrice + "&des=" + menuDescription + "&count=" + menuCount + "";
                Log.i(TAG, client_site);
                nm.postInfo(client_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                while (true) { // thread 작업이 끝날 때까지 대기
                    if (nm.isEnd) {
                        break;
                    }
                    Log.i(TAG, "아직 작업 안끝남.");
                }

                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i(TAG, "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                    builder.setMessage("등록에 실패하셨습니다.푸하하")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                    builder.setMessage("등록에 성공하셨습니다.")
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

    private void deleteShopMenu(int[] index){
        try {
            NetworkManager nm = new NetworkManager();
            String client_site = "/ownMenu/del?id=" + shopID + "&name=" + deleteMenu + "";
            Log.i(TAG, client_site);
            nm.postInfo(client_site, "POST"); //받은 placeId에 따른 장소 세부 정보

            while (true) { // thread 작업이 끝날 때까지 대기
                if (nm.isEnd) {
                    break;
                }
                Log.i(TAG, "아직 작업 안끝남.");
            }

            JSONObject jsonObject = nm.getResult();
            String success = jsonObject.getString("result");
            Log.i(TAG, "서버에서 받아온 result = " + success);

            if (success.equals("ERROR")) {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                builder.setMessage("삭제에 실패하셨습니다.푸하하")
                        .setNegativeButton("다시 시도", null)
                        .create()
                        .show();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                builder.setMessage("삭제에 성공하셨습니다.")
                        .setPositiveButton("확인", null)
                        .create()
                        .show();
                adapter.deldete(index[0]);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // adapter의 값이 변경되었다는 것을 알려줍니다.
                        adapter.notifyDataSetChanged();
                    }
                });
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}
