package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class LoginCustomerActivity extends AppCompatActivity {
    private static final String TAG = "LoginCustomerActivity";
    private AlertDialog.Builder builder;
    String userID;
    String userPasswd;
    String getUserPasswd;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);

        builder = new AlertDialog.Builder(LoginCustomerActivity.this);
        handler = new Handler();

        //여기까지 네트워크 소스
        final EditText idText = (EditText) findViewById(R.id.idText);
        final EditText passwordText = (EditText) findViewById(R.id.passwordText);
        passwordText.setTransformationMethod(new AsteriskPasswordTransformationMethod());
        final Button loginButton = (Button) findViewById(R.id.loginButton);
        final Button registerButton = (Button) findViewById(R.id.registerButton);

        //회원가입 버튼 클릭하면 사용자회원가입 액티비티로 넘어감
        registerButton.setOnClickListener(new View.OnClickListener() {

            @Override
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginCustomerActivity.this, RegisterCustomerActivity.class);
                LoginCustomerActivity.this.startActivity(registerIntent);
            }
        });

        //로그인 버튼 클릭하면 메인화면으로 넘어감
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            // 클릭 시 loginIntent 를 통해서 MainActivity를 실행
            public void onClick(View view) {
                userID = idText.getText().toString();
                userPasswd = passwordText.getText().toString();

                //아이디, 비번 입력 안 한 경우 확인 메세지 나오게 함
                if(idText.getText().toString().equals("") || passwordText.getText().toString().equals("")){
                    Log.i("LOGIN", "아이디 비번 빈칸");
                    builder.setMessage(" 아이디 및 비밀번호를 확인해주세요. ")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                }
                else{
                    try {
                        NetworkManager.add(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    String site = NetworkManager.url + "/user/login";
                                    site += "?id=" + userID + "&passwd=" + userPasswd;
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
                                            Log.i(TAG, " 서버: " + rec_data);

                                            if (rec_data.equals("")) {
                                                Log.i(TAG,"아이디가 틀렸습니다.");
                                            } else {
                                                // 객체를 추출한다.(장소하나의 정보)
                                                JSONArray root = new JSONArray(rec_data);
                                                JSONObject obj1 = root.getJSONObject(0);
                                                getUserPasswd = obj1.getString("passwd");

                                                if (getUserPasswd.equals(userPasswd)) {
                                                    Log.i(TAG, "로그인에 성공하셨습니다.");
                                                    startActivityWithID(MainActivity.class, userID);
                                                } else {
                                                    Log.i(TAG, "비밀번호가 틀렸습니다.");
                                                    Handler mHandler = new Handler(Looper.getMainLooper());
                                                    mHandler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            builder.setMessage("아이디나 비밀번호가 틀렸습니다.")
                                                                    .setNegativeButton("확인", null)
                                                                    .create()
                                                                    .show();
                                                        }
                                                    }, 0);
                                                }
                                            }
                                        }
                                    }
                                } catch (MalformedURLException e) {
                                    e.printStackTrace();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (JSONException e) {// for mapToJson()
                                    e.printStackTrace();
                                }
                            }
                        });
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private void startActivityWithID(Class c, String s) {
        Intent intent = new Intent(LoginCustomerActivity.this, c);
        intent.putExtra("userID", s);
        startActivity(intent);
    }


}