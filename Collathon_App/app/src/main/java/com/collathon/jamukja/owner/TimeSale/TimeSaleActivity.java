package com.collathon.jamukja.owner.TimeSale;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.collathon.jamukja.LoginCustomerActivity;
import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.RegisterCustomerActivity;
import com.collathon.jamukja.customer.reservation.ReservationActivity;
import com.collathon.jamukja.customer.store.category.list.Data;
import com.collathon.jamukja.owner.BlackList.BlackAdapter;
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
import java.util.ArrayList;
import java.util.List;

public class TimeSaleActivity extends AppCompatActivity {
    private static final String TAG = "TimeSaleActivity";
    private ListViewAdapter adapter;
    TextView timeID, percentID;
    private String shopID, shopName;
    private String check_time="0", check_percent = "0%";
    int selected = 0;
    List<String> menu_name_list, menu_price_list;
    ArrayList<TimeSaleData> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_time_sale_list);

        Intent intent = getIntent();
        shopID = intent.getExtras().getString("shopID");
        shopName = intent.getExtras().getString("shopName");

        // listview 생성 및 adapter 지정.
        final ListView listview = (ListView) findViewById(R.id.timeSaleListView) ;
        adapter = new ListViewAdapter();

        final TextView shoName = findViewById(R.id.time_sale_shopName);
        shoName.setText(shopName);

        timeID = findViewById(R.id.timesale_timeID);
        percentID = findViewById(R.id.timesale_percentID);

        findViewById(R.id.timesale_timeButton).setOnClickListener(onClickListener);
        findViewById(R.id.timesale_percentButton).setOnClickListener(onClickListener);

//        final ArrayList<TimeSaleData> items = new ArrayList<TimeSaleData>() ;
//        // ArrayAdapter 생성. 아이템 View를 선택(multiple choice)가능하도록 만듦.
//        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, items) ;

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


        final Button saveTimeSale = (Button)findViewById(R.id.timesale_saveButton) ;
        saveTimeSale.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checkedItems = listview.getCheckedItemPositions();
                int count = adapter.getCount() ;

                for (int i = count-1; i >= 0; i--) {
                    if (checkedItems.get(i)) {
                        String menu = menu_name_list.get(i);
                        String temp_price = menu_price_list.get(i);
                        int price = Integer.valueOf(temp_price);
                        Log.i("현재 선택 메뉴는", menu+", 가격 :"+price);

                        registerTimSale(menu, price);
                    }
                }
            }
        });


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.timesale_timeButton:
                    selectTime();
                    break;

                case R.id.timesale_percentButton:
                    selectPercent();
                    break;
            }
        }
    };


    private void getStockMenu() {
        //카테고리에 따른 가게 출력
        menu_name_list = new ArrayList<>();
        menu_price_list = new ArrayList<>();

        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/timesale/stock";
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
                                    String menu_name = jsonObject.getString("name");
                                    Log.i(TAG, "메뉴 이름: "+menu_name);

                                    String temp_price = jsonObject.getString("price");
                                    int menu_price = Integer.parseInt(temp_price);
                                    Log.i(TAG, "메뉴 가격: "+temp_price);

                                    menu_name_list.add(menu_name);
                                    menu_price_list.add(temp_price);

                                    // 각 List의 값들을 data 객체에 set 해줍니다.
                                    TimeSaleData data = new TimeSaleData();
                                    data.setShopID(Integer.parseInt(shopID));
                                    data.setMenuName(menu_name);
                                    data.setMenuPrice(menu_price);

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

    private void selectTime(){
        final String[] time = {"10:00-11:00", "11:00-12:00", "12:00-13:00", "13:00-14:00", "14:00-15:00", "15:00-16:00", "16:00-17:00",
                "17:00-18:00", "18:00-19:00", "19:00-20:00", "20:00-21:00", "21:00-22:00"};
        final int[] selectedIndex = {0};
        selected = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(TimeSaleActivity.this);
        builder.setTitle("예약 시간 선택")
                .setSingleChoiceItems(time, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedIndex[0] = which;
                        Log.i("RESERVATION", "which : "+which+", selectedIndex[0] : " +selectedIndex[0]);
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(TimeSaleActivity.this, time[selectedIndex[0]],
                                Toast.LENGTH_SHORT).show();
                        check_time = time[selectedIndex[0]];
                        Log.i("RESERVATION", "RESERVATION TIME : " + check_time);
                        selected = selectedIndex[0];
                        timeID.setText(check_time);

                        adapter.saleTime = check_time;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).create().show();
    }

    private void selectPercent(){
        final String[] time = {"10%", "20%", "30%", "40%", "50%", "60%", "70%", "80%" };
        final int[] selectedIndex = {0};
        selected = 0;

        AlertDialog.Builder builder = new AlertDialog.Builder(TimeSaleActivity.this);
        builder.setTitle("할인율 선택")
                .setSingleChoiceItems(time, selected, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        selectedIndex[0] = which;
                        Log.i(TAG, "which : "+which+", selectedIndex[0] : " +selectedIndex[0]);
                    }
                })
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        Toast.makeText(TimeSaleActivity.this, time[selectedIndex[0]],
                                Toast.LENGTH_SHORT).show();
                        check_percent = time[selectedIndex[0]];
                        Log.i(TAG, "할인율 : " + check_percent);
                        selected = selectedIndex[0];

                        percentID.setText(check_percent);
                        String temp = check_percent.replace("%", "");
                        int percent = Integer.valueOf(temp);
                        adapter.salePercent = percent;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                }).create().show();
    }

    private void registerTimSale(String name, int price) {
      //타임세일 등록addTimeSale(shop_id, name, sale_price, time)

        ///timesale/add?shop={shop_id}&menu={menu_name}&price={menu_salePrice}&saletime={menu_saleTime}

        String time = adapter.saleTime;
        int percent = adapter.salePercent;
        int saleprice = (int) (price * (1 - ((double)percent * 0.01)));
        Log.i("등록 가격", ""+saleprice);

        try {
            NetworkManager nm = new NetworkManager();
            Log.i("디비에 등록 데이터 : ", shopID+", "+name+","+saleprice+", "+time);

            if (name.length() > 0 && price > 0 && TextUtils.isEmpty(time)==false && time.length() > 0 && percent > 0)
            {
                String site = "/timesale/add?shop=" + shopID + "&menu=" + name + "&price="
                        + saleprice + "&saletime=" + time;
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
                    builder.setMessage("타임세일 등록에 실패하셨습니다.푸하하")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(TimeSaleActivity.this);
                    builder.setMessage("타임세일 등록에 성공하셨습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            } else if (TextUtils.isEmpty(time)){
                startToast("타임세일 시간을 선택하세요.");
            } else if (percent==0){
                startToast("타임세일 할인율을 선택하세요.");
            } else {
                startToast("입력하지 않은 정보가 존재합니다.");
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

