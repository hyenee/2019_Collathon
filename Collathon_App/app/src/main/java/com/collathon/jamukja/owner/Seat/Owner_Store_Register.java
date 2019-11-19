package com.collathon.jamukja.owner.Seat;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.owner.MainOwnerActivity;
import com.collathon.jamukja.owner.Store_info;
import com.collathon.janolja.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Owner_Store_Register extends AppCompatActivity {
    String ownerID;
    String str_title, str_tel, str_addr, str_cate, str_table;
    String str_t1,str_t2,str_t4;
    EditText textView_storeTitle, textView_storeTel, textView_storeAddr, textView_storeCate, textView_storeTable;
    EditText textView_t1, textView_t2, textView_t4;
    Button btn_regiser, btn_Y, btn_N, btn_remove;
    String success1="null", success2="null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_store_register);
        Intent intent = getIntent();
        ownerID = intent.getExtras().getString("ownerID");

        textView_storeTitle = findViewById(R.id.et_storeTitle);
        textView_storeTel = findViewById(R.id.et_storeTel);
        textView_storeAddr = findViewById(R.id.et_storeAddr);
        textView_storeCate = findViewById(R.id.et_storeCategory);
        textView_t1 = findViewById(R.id.et_t1);
        textView_t2 = findViewById(R.id.et_t2);
        textView_t4 = findViewById(R.id.et_t4);
        btn_Y = findViewById(R.id.btn_yes);
        btn_N = findViewById(R.id.btn_no);
        btn_regiser = findViewById(R.id.btn_regi_store);

        btn_N.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_t1.setVisibility(View.GONE);
                textView_t2.setVisibility(View.GONE);
                textView_t4.setVisibility(View.GONE);
                str_table = btn_N.getText().toString();


                str_title = textView_storeTitle.getText().toString();
                str_tel = textView_storeTel.getText().toString();
                str_addr = textView_storeAddr.getText().toString();
                str_cate = textView_storeCate.getText().toString();

                try {
                    NetworkManager nm = new NetworkManager();
                    if (str_title.length() > 0 && str_cate.length() > 0 && str_addr.length() > 0
                            && str_tel.length() > 0) {
                        String owner_site = "/ownShop/add?id=" + ownerID + "&name=" + str_title + "&tel="
                                + str_tel + "&addr=" + str_addr + "&category=" + str_cate + "&table=" + str_table;
                        Log.i("STORE", "SITE= " + owner_site);
                        nm.postInfo(owner_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                        while (true) { // thread 작업이 끝날 때까지 대기
                            if (nm.isEnd) {
                                break;
                            }
                            Log.i("STORE", "아직 작업 안끝남.");
                        }

                        JSONObject jsonObject = nm.getResult();
                        success1 = jsonObject.getString("result");
                        Log.i("STORE", "서버에서 받아온 result = " + success1);

                    } else {
                        startToast("입력하지 않은 정보가 존재합니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        btn_Y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToast("테이블이 있따구!");
                str_table = btn_Y.getText().toString();
                str_title = textView_storeTitle.getText().toString();
                str_tel = textView_storeTel.getText().toString();
                str_addr = textView_storeAddr.getText().toString();
                str_cate = textView_storeCate.getText().toString();


                try {
                    NetworkManager nm = new NetworkManager();
                    if (str_title.length() > 0 && str_cate.length() > 0 && str_addr.length() > 0
                            && str_tel.length() > 0) {
                        String owner_site = "/ownShop/add?id=" + ownerID + "&name=" + str_title + "&tel="
                                + str_tel + "&addr=" + str_addr + "&category=" + str_cate + "&table=" + str_table;
                        Log.i("STORE", "SITE= " + owner_site);
                        nm.postInfo(owner_site, "POST"); //받은 placeId에 따른 장소 세부 정보

                        while (true) { // thread 작업이 끝날 때까지 대기
                            if (nm.isEnd) {
                                break;
                            }
                            Log.i("STORE", "아직 작업 안끝남.");
                        }

                        JSONObject jsonObject = nm.getResult();
                        success2 = jsonObject.getString("result");


                    } else {
                        startToast("입력하지 않은 정보가 존재합니다.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//
            }
        });


        btn_regiser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startToast("여기뭔지 보자"+str_table);

                    if(success1.equals(null)){

                    }else{
                        if (success1.equals("ERROR")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Store_Register.this);
                            builder.setMessage("가게 등록에 실패하셨습니다.")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();

                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Store_Register.this);
                            builder.setMessage("가게 등록에 성공하셨습니다.")
                                    .setPositiveButton("확인", null)
                                    .create()
                                    .show();
                            Intent intent = new Intent(Owner_Store_Register.this, MainOwnerActivity.class);
                            intent.putExtra("Owner_id", ownerID);
                            startActivity(intent);
                        }
                    }

                    if(success2.equals(null)){

                    }else{
                        if(success2.equals("ERROR")){
                            AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Store_Register.this);
                            builder.setMessage("가게 정보등록에 실패하셨습니다.푸하하")
                                    .setNegativeButton("다시 시도", null)
                                    .create()
                                    .show();
                        }else {

                            startToast("여기 잘 들어가냐?");
                            setTable();

                        }

                    }
                }



        });

    }

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void setTable(){
        try {
            NetworkManager nm = new NetworkManager();
            str_t1 = textView_t1.getText().toString();
            str_t2 = textView_t2.getText().toString();
            str_t4 = textView_t4.getText().toString();

            if (str_t1.length() > 0 && str_t2.length() > 0 && str_t4.length() > 0) {
                String owner_site1 = "/ownShop/add/table?number=1"  + "&count=" + str_t1;
                String owner_site2 = "/ownShop/add/table?number=2"  + "&count=" + str_t2;
                String owner_site4 = "/ownShop/add/table?number=4"  + "&count=" + str_t4;

                nm.postInfo(owner_site1, "POST");
                nm.postInfo(owner_site2, "POST");
                nm.postInfo(owner_site4, "POST");//받은 placeId에 따른 장소 세부 정보

                while (true) { // thread 작업이 끝날 때까지 대기
                    if (nm.isEnd) {
                        break;
                    }
                }

                JSONObject jsonObject = nm.getResult();
                String success3 = jsonObject.getString("result");
                if(success3.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Store_Register.this);
                    builder.setMessage("가게 등록에 실패하셨습니다.푸하하")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(Owner_Store_Register.this);
                    builder.setMessage("가게와 테이블 등록에 성공하셨습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                    Intent intent = new Intent(Owner_Store_Register.this, MainOwnerActivity.class);
                    intent.putExtra("Owner_id", ownerID);
                    startActivity(intent);
                }


            } else {
                startToast("바보야 여기다");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
