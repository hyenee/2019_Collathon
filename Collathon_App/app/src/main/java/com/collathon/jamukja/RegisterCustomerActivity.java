package com.collathon.jamukja;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.collathon.janolja.R;


public class RegisterCustomerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_customer);

        final EditText nameText = (EditText)findViewById(R.id.nameText); //이름
        final EditText idText = (EditText)findViewById(R.id.idText); //아이디
        final EditText passwordText = (EditText)findViewById(R.id.passwordText); //비밀번호
        final EditText phoneText = (EditText)findViewById(R.id.phoneText); //전화번호
        final EditText emailText = (EditText)findViewById(R.id.emailText); //이메일

        Button registerButton = (Button)findViewById(R.id.registerButton);

        //회원가입 버튼 누르면 고객 회원가입으로 넘어감
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterCustomerActivity.this, LoginCustomerActivity.class);
                startActivity(intent);
            }
        });

    }
}