package com.collathon.jamukja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.collathon.janolja.R;

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
            // 클릭 시 registerIntent 를 통해서 registerActivity를 실행
            public void onClick(View view) {
                userID = idText.getText().toString();
                userPasswd = passwordText.getText().toString();

                try {
                    NetworkManager.add(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                String site = NetworkManager.url + "/user/login";
                                site += "?id=1&passwd=1111";
                                Log.i("LOGIN", site);

                                URL url = new URL(site);
                                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                                if (connection != null) {
                                    connection.setConnectTimeout(2000);
                                    connection.setUseCaches(false);
                                    if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                                        Log.i("LOGIN", "서버 연결됨");
                                        // 스트림 추출
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
                                        Log.i("LOGIN", " 서버: " + rec_data);

                                    }
                                }
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Intent loginIntent = new Intent(LoginCustomerActivity.this, MainActivity.class);
                //LoginCustomerActivity.this.startActivity(loginIntent);

            }
        });
    }

}