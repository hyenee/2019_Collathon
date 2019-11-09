package com.collathon.jamukja;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class LoginCustomerActivity extends AppCompatActivity {
    String customerID, customerPasswd;
    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_customer);
        builder = new AlertDialog.Builder(LoginCustomerActivity.this);

        final EditText idText = (EditText)findViewById(R.id.idText);
        final EditText passwordText = (EditText)findViewById(R.id.passwordText);
        final Button loginButton = (Button)findViewById(R.id.loginButton);
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
                Intent loginIntent = new Intent(LoginCustomerActivity.this, MainActivity.class);
                LoginCustomerActivity.this.startActivity(loginIntent);
            }

            /*
            @Override
            public void onClick(View view) {
                customerID = idText.getText().toString();
                customerPasswd = passwordText.getText().toString();
                if(idText.getText().toString().equals("") || passwordText.getText().toString().equals("")){
                    builder.setMessage("아이디 및 비밀번호를 확인해주세요. ")
                            .setNegativeButton("확인", null)
                            .create()
                            .show();
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                //제이슨 생성
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    Log.d("로그인 성공", "");
                                    customerID = jsonResponse.getString("id");
                                    customerPasswd = jsonResponse.getString("passwd");
                                    SharedPreferences auto = getSharedPreferences("auto", AppCompatActivity.MODE_PRIVATE);
                                    //auto의 loginId와 loginPwd에 값을 저장해 줍니다.
                                    SharedPreferences.Editor autoLogin = auto.edit();
                                    autoLogin.putString("userID", customerID);
                                    autoLogin.commit();
                                    Log.d("로그인", "아이디 : " + customerID);

                                    Intent loginIntent = new Intent(LoginCustomerActivity.this, MainActivity.class);
                                    LoginCustomerActivity.this.startActivity(loginIntent);
                                    finish();
                                } else {
                                    builder.setMessage("아이디 및 비밀번호를 확인해주세요. ")
                                            .setNegativeButton("확인", null)
                                            .create()
                                            .show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    //로그인 리퀘스트 생성
                 //   LoginCustomerRequest loginRequest = new LoginCustomerRequest(customerID, customerPasswd, responseListener);
                  //  RequestQueue queue = Volley.newRequestQueue(LoginCustomerActivity.this);

                    // 요청
                  //  queue.add(loginRequest);
                }


                        }
                        */
                    });

        }
    }