package com.collathon.jamukja.owner.Seat;



import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.collathon.janolja.R;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class FirstManagerAdapter extends RecyclerView.Adapter<FirstManagerAdapter.ItemViewHolder>  {
    private ArrayList<BookData> listData = new ArrayList<>();
    private ArrayList<BookData> listData_over = new ArrayList<>();


    private BookData data;
    private Context context;
    Bundle bundle;


    @NonNull
    @Override
    public FirstManagerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_list_seat, parent, false);

        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FirstManagerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(BookData data) {
        // 외부에서 item을 추가시킬 함수입니다.

        listData.add(data);
    }
    void addOverItem(BookData data){
        listData_over.add(data);
    }
    void deldete(int str){
        listData.remove(str);
    }
    BookData getData(int temp){
        return listData.get(temp);
    }
    ArrayList<BookData> getListData(){
        return listData;
    }
    void getData(){

    }





    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1,textView2;
        private BookData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.et_book_id);
            textView2 = itemView.findViewById(R.id.et_book_time);
        }

        void onBind(BookData data) {
            this.data = data;

            textView1.setText(data.getName());
            textView2.setText(data.getTime());


            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i("STORE", String.valueOf(v.getId()));
            Log.i("STORE","뭔지 알쥐?"+ String.valueOf(R.id.linear_userID));
            switch (v.getId()) {

                case R.id.et_book_id:
                    String temp = textView1.getText().toString();
                    Toast.makeText(context, data.getName(), Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    View view = LayoutInflater.from(context).inflate(R.layout.owner_reservation_popup, null, false);

                    View view_pop = LayoutInflater.from(context).inflate(R.layout.owner_list_seat_list, null, false);
                    ListView listView = view.findViewById(R.id.listview_pop);

                    List<String> list = new ArrayList<>();

                    int index = listData.indexOf(temp);

                    list.add(listData.get(index).table);
                    BookData book = new BookData();
                    book.setTable(listData.get(index).table);
                    book.setCount(listData.get(index).count);


                    builder.setView(view);


                    final AlertDialog dialog = builder.create();
                    dialog.show();
                    break;

            }
        }
    }
}