package com.collathon.jamukja.owner;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.janolja.R;

import java.util.Arrays;
import java.util.List;

public class Store_info extends AppCompatActivity {
    private TextView textView_storename;
    private SecondAdapter adapter;
    private Button btn_registerMenu, btn_remove;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.owner_store_info);
        textView_storename = findViewById(R.id.et_storename);
        Intent intent = getIntent();
        String name = intent.getExtras().getString("title");
        textView_storename.setText(name);
        init();
        getData();

        btn_registerMenu = findViewById(R.id.btn_registerMenu);
        btn_registerMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                View view = LayoutInflater.from(Store_info.this).inflate(R.layout.owner_menu_register, null, false);
                builder.setView(view);


                final Button ButtonSubmit = (Button) view.findViewById(R.id.btn_insert);
                final EditText editTextTitle = (EditText) view.findViewById(R.id.et_menuTitle);
                final EditText editTextEnglish = (EditText) view.findViewById(R.id.et_menuContent);
                final EditText editTextKorean = (EditText) view.findViewById(R.id.et_menuPrice);
                ButtonSubmit.setText("삽입");

                final AlertDialog dialog = builder.create();



                ButtonSubmit.setOnClickListener(new View.OnClickListener() {


                    public void onClick(View v) {


                        // 4. 사용자가 입력한 내용을 가져와서
                        String strID = editTextTitle.getText().toString();
                        String strEnglish = editTextEnglish.getText().toString();
                        String strKorean = editTextKorean.getText().toString();


                        // 5. ArrayList에 추가하고

                        Data dict = new Data();
                        dict.setTitle(strID);
                        dict.setContent(strEnglish);
                        dict.setPrice(strKorean);

                        adapter.addItem(dict); //첫번째 줄에 삽입됨
                        //mArrayList.add(dict); //마지막 줄에 삽입됨


                        // 6. 어댑터에서 RecyclerView에 반영하도록 합니다.

                        adapter.notifyItemInserted(0);
                        //mAdapter.notifyDataSetChanged();

                        dialog.dismiss();
                    }
                });

                dialog.show();


            }
        });
        btn_remove = findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(Store_info.this);
                final String temp[]= new String[adapter.getItemCount()];
                final int[] index = {0};
                for(int i =0; i< temp.length; i++){
                    temp[i] = adapter.getData(i).getTitle();
                }
                builder.setTitle("삭제할 항목을 고르시오.").setSingleChoiceItems(temp, 0,new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {

                        index[0] = item;
                        Toast.makeText(getApplicationContext(), "Phone Model = "+temp[item], Toast.LENGTH_SHORT).show();
                        //adapter.remove(data);
                    }
                });
                builder.setNegativeButton("삭제", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.v("가져옴", String.valueOf(index[0]));
                        Toast.makeText(getApplicationContext(), "Phone Model = "+index[0], Toast.LENGTH_SHORT).show();
                        adapter.deldete(index[0]);

                        adapter.notifyDataSetChanged();

                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }
    private void init() {
        RecyclerView recyclerView = findViewById(R.id.recycler_menu);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SecondAdapter();
        recyclerView.setAdapter(adapter);
    }

    private void getData() {
        // 임의의 데이터입니다.
        List<String> listTitle = Arrays.asList("안녕", "만나서", "반가워", "헬로우", "아자!");
        List<String> listContent = Arrays.asList(
                "이 꽃은 국화입니다.",
                "여기는 사막입니다.",
                "이 꽃은 수국입니다.",
                "이 동물은 해파리입니다.",
                "이 동물은 코알라입니다."
        );
        List<String> listPrice = Arrays.asList(
                "8000won.",
                "4000won.",
                "7500won.",
                "35,000won.",
                "3000won."
        );

        for (int i = 0; i < listTitle.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줍니다.
            Data data = new Data();
            data.setTitle(listTitle.get(i));
            data.setContent(listContent.get(i));
            data.setPrice(listPrice.get(i));

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }
        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();
    }
}
