package com.collathon.jamukja.customer.reservation;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.janolja.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    private int selected = 0;
    private String menu_count = "0";

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation_view, parent, false);
        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        for(int i=0; i<listData.size(); i++){
            Log.i("RESERVATION", "addItem :"+ listData.get(i).getName()+", "+ listData.get(i).getPrice()+", " +listData.get(i).getCount());
        }
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;
        private TextView remain_count;
        private TextView menu_number;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.reservation_menu_name);
            price = (TextView)itemView.findViewById(R.id.reservation_menu_price);
            remain_count = (TextView)itemView.findViewById(R.id.reservation_remaining_count);
            menu_number = (TextView)itemView.findViewById(R.id.reservation_menu_number);
        }

        void onBind(Data data) {
            this.data = data;

            name.setText(data.getName());
            price.setText(data.getPrice());
            remain_count.setText(data.getCount());
            menu_number.setText("0");

            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectMenuNumber();
                }
            });
        }

        public void selectMenuNumber(){
            final String[] time = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
            final int[] selectedIndex = {0};

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("구매 수량 선택")
                    .setSingleChoiceItems(time, selected, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            selectedIndex[0] = which;
                            Log.i("RESERVATION", "which : "+which+", selectedIndex[0] : " +selectedIndex[0]);
                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int which) {
                            Toast.makeText(context, time[selectedIndex[0]], Toast.LENGTH_SHORT).show();
                            menu_count = time[selectedIndex[0]];
                            Log.i("RESERVATION", "MENU COUNT : " + menu_count);
                            selected = selectedIndex[0];
                            menu_number.setText(menu_count); //time_id 화면에 보내줌
                        }
                    }).create().show();
            //reservation_time = time[selectedIndex[0]];
            Log.i("RESERVATION", "MENU COUNT : " + menu_count);
        }

    }

}
