package com.collathon.jamukja.owner;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.LoginOwnerActivity;
import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.owner.BlackList.Owner_BlackList;
import com.collathon.jamukja.owner.confirm.Owner_Reservation_Manager;
import com.collathon.jamukja.owner.Seat.Owner_Store_Register;
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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainOwnerActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long backKeyPressedTime  = 0;
    private RecyclerAdapter adapter;
    private TextView textView_ownerName;
    private Toast toast;
    private String ownerID, ownerName,deleteMenu;
    String shop_name;
    String shop_id;
    String delete_shop_id;
    String index_delete[];


    Button btn_store_register, btn_store_remove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_main);

        Intent intent = getIntent();
        ownerID = intent.getExtras().getString("Owner_id");
        ownerName = intent.getExtras().getString("OwnerName");

        textView_ownerName = findViewById(R.id.user_name);
        textView_ownerName.setText(ownerName);

        setRecyclerView();
        getData();

        //각 버튼 클릭시 해당하는 페이지로 넘어가게 하는 코드
        findViewById(R.id.blackButton).setOnClickListener(onClickListener);
        findViewById(R.id.bookButton).setOnClickListener(onClickListener);
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener);

        btn_store_register = findViewById(R.id.btn_regi_store);
        btn_store_remove = findViewById(R.id.btn_delete_store);

        btn_store_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainOwnerActivity.this, Owner_Store_Register.class);
                intent1.putExtra("ownerID", ownerID);
                startActivity(intent1);
            }
        });
        btn_store_remove.setOnClickListener(new View.OnClickListener() {
            int len ;

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(MainOwnerActivity.this);
                final String temp[] = new String[adapter.getItemCount()];
                index_delete = new String[adapter.getItemCount()];
                final int[] index = {0};
                for (int i = 0; i < temp.length; i++) {
                    temp[i] = adapter.getData(i).getTitle();
                    index_delete[i]= adapter.getData(i).getId();
                    Log.i("STORE",index_delete[i]);
                }
                len = temp.length;
                builder.setTitle("삭제할 항목을 고르시오.").setSingleChoiceItems(temp, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        index[0] = item;
                        Toast.makeText(getApplicationContext(), "어디냐 " + index[0], Toast.LENGTH_SHORT).show();

                        deleteMenu = temp[item];
                        delete_shop_id = index_delete[item];
                        startToast(delete_shop_id);

                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("가져옴", String.valueOf(index[0]));
                        Toast.makeText(getApplicationContext(), "Phone Model = " + index[0], Toast.LENGTH_SHORT).show();

                        try {

                            NetworkManager nm = new NetworkManager();

                            String client_site = "/ownShop/delete?shop=" + delete_shop_id ;
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
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainOwnerActivity.this);
                                builder.setMessage("삭제에 실패하셨습니다. 등록된 예약이나 타임세일이 있는지 확인하세요")
                                        .setNegativeButton("확인", null)
                                        .create()
                                        .show();
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(MainOwnerActivity.this);
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
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.blackButton:
                    Intent blackintent = new Intent(MainOwnerActivity.this, Owner_BlackList.class);
                    startActivity(blackintent);
                    break;

                case R.id.bookButton:
                    Intent intent1 = new Intent(MainOwnerActivity.this, Owner_Reservation_Manager.class);
                    intent1.putExtra("owner_id", ownerID);
                    startActivity(intent1);
                    break;


                case R.id.logoutButton:
                    new AlertDialog.Builder(MainOwnerActivity.this)
                            .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent logoutIntent = new Intent(MainOwnerActivity.this, LoginOwnerActivity.class);
                                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    logoutIntent.putExtra( "KILL", true );
                                    startActivity(logoutIntent);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();

                    break;
            }
        }
    };
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setRecyclerView() {
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
                        site += "?id=" + ownerID;
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

                                // 읽어온다.
                                do {
                                    str = br.readLine();
                                    if (str != null) {
                                        buf.append(str);
                                    }
                                } while (str != null);
                                br.close(); // 스트림 해제

                                String rec_data = buf.toString();
                                Log.i(TAG, "서버: " + rec_data);
                                JSONArray jsonArray = new JSONArray(rec_data);
                                index_delete = new String[jsonArray.length()];

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    shop_id = jsonObject.getString("id");
                                    shop_name = jsonObject.getString("name");
                                    index_delete[i] = shop_id;
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
    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }

    private void showGuide() {
        toast = Toast.makeText(MainOwnerActivity.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

}