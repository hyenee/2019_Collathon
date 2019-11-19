package com.collathon.jamukja.owner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.customer.user_info.customer.CustomerInfoActivity;
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

public class Owner_info_change extends AppCompatActivity {
    private static final String TAG = "CustomerInfoActivity";
    Handler handler;
    TextView owner_name, owner_phone, owner_id;
    EditText owner_passwd;
    Button changeButton;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_info_change);
        handler = new Handler();

        final Intent intent = getIntent(); /*데이터 수신*/
        userID = intent.getExtras().getString("owner_id"); /*String형*/

        owner_name = (TextView)findViewById(R.id.owner_name); //사용자 이름
        owner_phone = (TextView)findViewById(R.id.owner_phone); //사용자 핸드폰
        owner_id = (TextView)findViewById(R.id.owner_id); //사용자 아이디
        owner_passwd = (EditText)findViewById(R.id.owner_passwd); //사용자 비밀번호
        changeButton = (Button)findViewById(R.id.btn_infoChange); //비밀번호 변경 버튼

        getCustomInfo();

        //비밀번호 수정
        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeInfo();
                getCustomInfo();
            }
        });
    }

    private void getCustomInfo(){
        //서버에서 회원 정보 GET
        try {
            NetworkManager.add(new Runnable() {
                @Override
                public void run() {
                    try {
                        String site = NetworkManager.url + "/mypage/owner";
                        site += "?id="+userID;
                        Log.i("MY", site);

                        URL url = new URL(site);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                        if (connection != null) {
                            connection.setConnectTimeout(2000);
                            connection.setUseCaches(false);
                            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                Log.i("MY", "서버 연결됨");
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
                                Log.i("MY, ", "서버: " + rec_data);

                                JSONArray jsonArray = new JSONArray(rec_data);
                                JSONObject jsonObject = jsonArray.getJSONObject(0);
                                final String name = jsonObject.getString("name");
                                final String id = jsonObject.getString("id");
                                final String passwd = jsonObject.getString("passwd");
                                final String phone = jsonObject.getString("phone");
                                Log.i("MY", "추출 결과 :  " + name+", "+id+", "+passwd+", "+phone);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        owner_name.setText(name);
                                        owner_id.setText(id);
                                        owner_passwd.setText(passwd);
                                        owner_phone.setText(phone);
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


    private void changeInfo(){
        String newPasswd = owner_passwd.getText().toString();

        //비밀번호 변경 POST
        int passwdlen = owner_passwd.length();
        Log.i("MY", "PASSWD : "+ passwdlen+", "+newPasswd);
        try {
            NetworkManager nm = new NetworkManager();
            if (passwdlen>0) {
                String owner_site = "/mypage/owner?id="+userID+ "&new=" + newPasswd;
                Log.i("MY", "SITE= "+owner_site);
                nm.postInfo(owner_site, "PATCH"); //받은 placeId에 따른 장소 세부 정보

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("MY", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("MY", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(Owner_info_change.this);
                    builder.setMessage("비밀번호 변경 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Owner_info_change.this);
                    builder.setMessage("비밀번호 변경 성공")
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

    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

}