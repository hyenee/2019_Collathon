package com.collathon.jamukja.owner.TimeSale.TimeSaleDelete;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
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
import java.util.ArrayList;
import java.util.List;

public class TimeSaleActivity extends AppCompatActivity {
    private static final String TAG = "TimeSaleDeleteActivity";
    private ListViewAdapter adapter;
    TextView timeID, percentID;
    private String shopID, shopName;
    List<String> menu_name_list, menu_price_list;
    List<Integer> timesaleID_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_time_sale_delete_list);

        Intent intent = getIntent();
        shopID = intent.getExtras().getString("shopID");
        shopName = intent.getExtras().getString("shopName");

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.timeSaleListView) ;
        adapter = new ListViewAdapter();

        final TextView shoName = findViewById(R.id.time_sale_shopName);
        shoName.setText(shopName);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                listview.setAdapter(adapter) ;
            }
        });

        listview.setAdapter(adapter) ;
        getStockMenu();

        // selectAll button에 대한 이벤트 처리.
        final Button selectAllButton = (Button)findViewById(R.id.selectAll) ;
        selectAllButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                int count = 0 ;
                count = adapter.getCount() ; // adapter에 사용되는 데이터의 개수

                for (int i=0; i<count; i++) {
                    listview.setItemChecked(i, true) ;
                }

            }
        }) ;

        final Button deleteTimeSale = (Button)findViewById(R.id.timesale_deleteButton) ;
        deleteTimeSale.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        int timesaleID = timesaleID_list.get(i);
                        Log.i("현재 선택 메뉴는", ""+timesaleID);

                        removeTimeSale(timesaleID);
                    }
                }
                adapter = new ListViewAdapter();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listview.setAdapter(adapter) ;
                    }
                });
                getStockMenu();
            }
        });

    }


    private void getStockMenu() {
        //카테고리에 따른 가게 출력
        menu_name_list = new ArrayList<>();
        menu_price_list = new ArrayList<>();
        timesaleID_list = new ArrayList<>();
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    ///timesale?shop={shop_id}
                    try {
                        String site = NetworkManager.url + "/timesale";
                        site += "?shop="+shopID;
                        Log.i("TimeSaleActivity SITE", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i(TAG, "서버 연결됨");

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
                                for(int i=0; i<jsonArray.length(); i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    int timesale_id = jsonObject.getInt("id");
                                    String menu_name = jsonObject.getString("name");
                                    int sale_price = jsonObject.getInt("sale_price");
                                    String time = jsonObject.getString("time");
                                    int menu_count = jsonObject.getInt("count");

                                    Log.i(TAG, "타임세일 정보: "+menu_name+"," +sale_price+", "+time+", "+menu_count);

                                    timesaleID_list.add(timesale_id);

                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    TimeSaleData data = new TimeSaleData();
                                    data.setShopID(Integer.parseInt(shopID));
                                    data.setMenuName(menu_name);
                                    data.setSalePrice(sale_price);
                                    data.setSaleTime(time);
                                    data.setMenuCount(menu_count);

                                    adapter.addItem(data);
                                    // 각 값이 들어간 data를 adapter에 추가합니다.
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

    private void removeTimeSale(int timesaleID){
        try {
            NetworkManager nm = new NetworkManager();

            if (timesaleID > 0 )
            {
                String site = "/timesale/delete?register=" + timesaleID;
                Log.i(TAG, "SITE= "+site);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeSaleActivity.this);
                    builder.setMessage("타임세일 삭제에 실패하셨습니다.")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeSaleActivity.this);
                    builder.setMessage("타임세일 삭제에 성공하셨습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            } else {
                startToast("삭제할 메뉴를 선택하세요.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}

