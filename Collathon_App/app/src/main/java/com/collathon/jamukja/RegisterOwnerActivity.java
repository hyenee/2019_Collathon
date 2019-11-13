package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterOwnerActivity extends AppCompatActivity {
    private static final String TAG = "RegisterOwnerActivity";
    private String userName, userId, userPasswd, userPhone, userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        findViewById(R.id.registerButton).setOnClickListener(onClickListener);
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //회원가입 버튼 누르면 고객 회원가입으로 넘어감
                case R.id.registerButton:
                    registerOwner();
                    break;
            }
        }
    };

    private void registerOwner(){
        userName = ((EditText) findViewById(R.id.nameText)).getText().toString(); // 유저가 입력한 id
        userId = ((EditText) findViewById(R.id.idText)).getText().toString(); // 유저가 입력한 id
        userPasswd = ((EditText) findViewById(R.id.passwordText)).getText().toString(); // 유저가 입력한 id
        userPhone = ((EditText) findViewById(R.id.phoneText)).getText().toString(); // 유저가 입력한 id
        try {
            NetworkManager nm = new NetworkManager();
            if (userName.length() > 0 && userId.length() > 0 && userPasswd.length() > 0
                    && userPhone.length() > 0) {
                String owner_site = "/owner/login?id=" + userId + "&passwd=" + userPasswd + "&name="
                        + userName + "&phone=" + userPhone;
                Log.i(TAG, "SITE= "+owner_site);
                nm.postInfo(owner_site, "POST"); //받은 placeId에 따른 장소 세부 정보

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
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterOwnerActivity.this);
                    builder.setMessage("회원 가입에 실패하셨습니다.푸하하")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();

                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterOwnerActivity.this);
                    builder.setMessage("회원 가입에 성공하셨습니다.")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();

                    Intent intent = new Intent(RegisterOwnerActivity.this, LoginOwnerActivity.class);
                    startActivity(intent);
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

