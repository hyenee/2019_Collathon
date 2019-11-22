package com.collathon.jamukja;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.collathon.janolja.R;

public class IntroActivity extends Activity {
    private static final String TAG = "IntroActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_layout); //xml , java 소스 연결

        NetworkManager.init(); //thread 시작

        Handler handler = new Handler();
        handler.postDelayed(new Runnable(){
            @Override
            public void run() {
                Intent intent = new Intent (getApplicationContext(), SelectUserActivity.class);
                startActivity(intent); //다음화면으로 넘어감
                finish();
            }
        },3000); //3초 뒤에 Runner객체 실행하도록 함
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}
