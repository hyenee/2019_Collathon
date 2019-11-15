package com.collathon.jamukja.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
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
import com.collathon.jamukja.RegisterCustomerActivity;
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

public class Store_info extends AppCompatActivity {
    private TextView textView_storename;
    private SecondAdapter adapter;
    private Button btn_registerMenu, btn_remove;
    private String name, ownerName, shopName,deleteMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_store_info);
        textView_storename = findViewById(R.id.et_storename);
        final Intent intent = getIntent();
        name = intent.getExtras().getString("title");
        ownerName = intent.getExtras().getString("owner_ID");
        shopName = intent.getExtras().getString("shop_id");

        textView_storename.setText(name);
        setRecyclerView();
        getData();

        btn_registerMenu = findViewById(R.id.btn_registerMenu);
        btn_registerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                View view = LayoutInflater.from(Store_info.this).inflate(R.layout.owner_menu_register, null, false);
                builder.setView(view);


                final Button ButtonSubmit = (Button) view.findViewById(R.id.btn_insert);
                final EditText editTextTitle = (EditText) view.findViewById(R.id.et_menuTitle);
                final EditText editTextDescription = (EditText) view.findViewById(R.id.et_menuContent);
                final EditText editTextPrice = (EditText) view.findViewById(R.id.et_menuPrice);
                final EditText editTextCount = (EditText) view.findViewById(R.id.et_menuCount);
                ButtonSubmit.setText("삽입");

                final AlertDialog dialog = builder.create();


                ButtonSubmit.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {


                        // 4. 사용자가 입력한 내용을 가져와서
                        String menuName = editTextTitle.getText().toString();
                        String menuDescription = editTextDescription.getText().toString();
                        String menuPrice = editTextPrice.getText().toString();
                        String menuCount = editTextCount.getText().toString();


                        // 5. ArrayList에 추가하고

                        MenuData dict = new MenuData();
                        dict.setTitle(menuName);
                        dict.setContent(menuDescription);
                        dict.setPrice(menuPrice);
                        dict.setCount(menuCount);
                        adapter.addItem(dict);
                        try {
                            NetworkManager nm = new NetworkManager();
                            if (menuName.length() > 0 && menuDescription.length() > 0 && menuPrice.length() > 0) {
                                String client_site = "/ownMenu/add?id=" + ownerName + "&name=" + menuName + "&price="
                                        + menuPrice + "&des=" + menuDescription + "&count=" + menuCount + "";
                                Log.i("STORE", client_site);
                                nm.postInfo(client_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                                while (true) { // thread 작업이 끝날 때까지 대기
                                    if (nm.isEnd) {
                                        break;
                                    }
                                    Log.i("STORE", "아직 작업 안끝남.");
                                }

                                JSONObject jsonObject = nm.getResult();
                                String success = jsonObject.getString("result");
                                Log.i("STORE", "서버에서 받아온 result = " + success);

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
                                    //Intent intent = new Intent(RegisterCustomerActivity.this, LoginCustomerActivity.class);
                                    //startActivity(intent);
                                }
                            } else {
                                startToast("입력하지 않은 정보가 존재합니다.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨


                        // 6. 어댑터에서 RecyclerView에 반영하도록 합니다.

                        //adapter.notifyItemChanged(0);
                        adapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });
        btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            int len ;
            @Override

            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                final String temp[] = new String[adapter.getItemCount()];
                final int[] index = {0};
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = adapter.getData(i).getTitle();
                }
                len = temp.length;
                builder.setTitle("삭제할 항목을 고르시오.").setSingleChoiceItems(temp, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        index[0] = item;
                        Toast.makeText(getApplicationContext(), "어디냐 " + temp[item], Toast.LENGTH_SHORT).show();
                        deleteMenu = temp[item];

                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("가져옴", String.valueOf(index[0]));
                        Toast.makeText(getApplicationContext(), "Phone Model = " + index[0], Toast.LENGTH_SHORT).show();



                        try {
                            NetworkManager nm = new NetworkManager();

                            String client_site = "/ownMenu/del?id=" + shopName + "&name=" + deleteMenu + "";
                            Log.i("STORE", client_site);
                            nm.postInfo(client_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                            while (true) { // thread 작업이 끝날 때까지 대기
                                if (nm.isEnd) {
                                    break;
                                }
                                Log.i("STORE", "아직 작업 안끝남.");
                            }

                            JSONObject jsonObject = nm.getResult();
                            String success = jsonObject.getString("result");
                            Log.i("STORE", "서버에서 받아온 result = " + success);

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
                        //adapter.notifyItemChanged(index[0]);


                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

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
        // 임의의 데이터입니다.
        final List<String> shop_name_list = new ArrayList<>();
        final List<String> menu_price_list = new ArrayList<>();
        final List<String> menu_des_list = new ArrayList<>();
        final List<String> menu_count_list = new ArrayList<>();


        //서버 파싱
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/ownMenu";
                        site += "?id=" + shopName;
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
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    String shop_name = jsonObject.getString("name");
                                    String menu_price = jsonObject.getString("price");
                                    String menu_des = jsonObject.getString("description");
                                    String menu_count = jsonObject.getString("count");
                                    Log.i("STORE", shop_name);
                                    shop_name_list.add(shop_name);
                                    menu_price_list.add(menu_price);
                                    menu_des_list.add(menu_des);
                                    menu_count_list.add(menu_count);
                                }
                                for (int i = 0; i < shop_name_list.size(); i++) {
                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    MenuData data = new MenuData();
                                    data.setTitle(shop_name_list.get(i));
                                    data.setCount(menu_count_list.get(i));
                                    data.setContent(menu_des_list.get(i));
                                    data.setPrice(menu_price_list.get(i));
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
                    } catch (JSONException e) {
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
