package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.collathon.janolja.R;

public class RegisterOwnerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_owner);

        final EditText nameText = (EditText)findViewById(R.id.nameText); //이름
        final EditText idText = (EditText)findViewById(R.id.idText); //아이디
        final EditText passwordText = (EditText)findViewById(R.id.passwordText); //비밀번호
        final EditText phoneText = (EditText)findViewById(R.id.phoneText); //전화번호

        Button registerButton = (Button)findViewById(R.id.registerButton);

        //회원가입 버튼 누르면 자영업자 회원가입으로 넘어감
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterOwnerActivity.this, LoginOwnerActivity.class);
                startActivity(intent);
            }
        });

    }

}
