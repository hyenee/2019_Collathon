package com.collathon.jamukja;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.collathon.jamukja.customer.store.category.list.StoreListActivity;
import com.collathon.jamukja.customer.user_info.customer.CustomerMyMenuActivity;
import com.collathon.janolja.R;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private long backKeyPressedTime  = 0;
    private Toast toast;
    private String userID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent(); /*데이터 수신*/
        userID = intent.getExtras().getString("userID"); /*String형*/

        //카테고리 버튼 클릭하면 이동, 순서대로 버튼 1~6
        findViewById(R.id.category_button_rice).setOnClickListener(onClickListener); //한식
        findViewById(R.id.category_button_noodle).setOnClickListener(onClickListener); //국수
        findViewById(R.id.category_button_chicken).setOnClickListener(onClickListener); //치킨
        findViewById(R.id.category_button_meat).setOnClickListener(onClickListener); //고기
        findViewById(R.id.category_button_dessert).setOnClickListener(onClickListener); //디저트
        findViewById(R.id.category_button_sushi).setOnClickListener(onClickListener); //스시

        //하단 메뉴바 버튼 클릭하면 이동
        findViewById(R.id.homeButton).setOnClickListener(onClickListener); //홈
        findViewById(R.id.pickButton).setOnClickListener(onClickListener); //찜 목록
        findViewById(R.id.myButton).setOnClickListener(onClickListener); //내 정보
        findViewById(R.id.logoutButton).setOnClickListener(onClickListener); //로그아웃

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.category_button_rice:
                    startActivityWithCategory(StoreListActivity.class, userID,"rice");
                    break;

                case R.id.category_button_noodle:
                    startActivityWithCategory(StoreListActivity.class, userID,"noodle");
                    break;

                case R.id.category_button_chicken:
                    startActivityWithCategory(StoreListActivity.class, userID,"chicken");
                    break;

                case R.id.category_button_meat:
                    startActivityWithCategory(StoreListActivity.class, userID,"meat");
                    break;

                case R.id.category_button_dessert:
                    startActivityWithCategory(StoreListActivity.class, userID,"dessert");
                    break;

                case R.id.category_button_sushi:
                    startActivityWithCategory(StoreListActivity.class, userID,"sushi");
                    break;

                    // 하단 메뉴바 이동
                case R.id.homeButton:
                    startActivity(MainActivity.class, userID);
                    break;

//                case R.id.pickButton:
//                    Intent noodleIntent = new Intent(MainActivity.this, StoreListActivity.class);
//                    MainActivity.this.startActivity(noodleIntent);
//                    break;

                case R.id.myButton:
                    startActivity(CustomerMyMenuActivity.class, userID);
                    break;

                case R.id.logoutButton:
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("로그아웃").setMessage("로그아웃 하시겠습니까?")
                            .setPositiveButton("로그아웃", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    Intent logoutIntent = new Intent(MainActivity.this, LoginCustomerActivity.class);
                                    logoutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    logoutIntent.putExtra( "KILL", true );
                                    startActivity(logoutIntent);
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {

                                }
                            })
                            .show();

                    break;
            }
        }
    };

    private void startActivity(Class c, String id){
        Intent intent = new Intent(MainActivity.this, c);
        intent.putExtra("userID", id);
        startActivity(intent);
    }

    private void startActivityWithCategory(Class c, String id, String category) {
        Intent intent = new Intent(MainActivity.this, c);
        intent.putExtra("userID", id);
        intent.putExtra("category", category);
        startActivity(intent);
    }

    private void showGuide() {
        toast = Toast.makeText(MainActivity.this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            this.finishAffinity();
            android.os.Process.killProcess(android.os.Process.myPid());
            System.runFinalization();
            System.exit(0);
            toast.cancel();
        }
    }


}
