package com.collathon.jamukja.owner.Seat;


import android.content.Context;
import android.content.Intent;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.owner.Data;
import com.collathon.jamukja.owner.MenuData;
import com.collathon.jamukja.owner.Seat.Owner_Reservation_Manager;
import com.collathon.jamukja.owner.Seat.Owner_Reservation_seat;
import com.collathon.jamukja.owner.Store_info;
import com.collathon.janolja.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();
    private TextView textView1;
    private ImageView imageView;
    private BookData data;
    private Context context;


    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.

        //View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_item_view_store, parent, false);


        View view2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_list_menu, parent, false);

        this.context = parent.getContext();
        return new RecyclerAdapter.ItemViewHolder(view2);
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.store_title);


        }

        void onBind(Data data) {
            this.data = data;

            textView1.setText(data.getTitle());

            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.store_title:
                    Intent intent2 = new Intent(v.getContext(), Owner_Seat_Info.class);
                    Log.i("STORE", "ownerID가 잘 들어오냐" + data.getOwner());
                    intent2.putExtra("shop_id", data.getId());
                    v.getContext().startActivity(intent2);
                    break;

            }
        }
    }
}