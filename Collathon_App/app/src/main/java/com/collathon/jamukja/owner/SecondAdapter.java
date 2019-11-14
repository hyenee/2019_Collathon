package com.collathon.jamukja.owner;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.janolja.R;

import java.util.ArrayList;

public class SecondAdapter extends RecyclerView.Adapter<SecondAdapter.ItemViewHolder>  {
    private ArrayList<MenuData> listData = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private Data data;
    private Context context;

    @NonNull
    @Override
    public SecondAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_item_view_menu, parent, false);
        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull SecondAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(MenuData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    void deldete(int str){
        Log.v("삭제", String.valueOf(listData.size()));
        listData.remove(str);

    }
    MenuData getData(int temp){

        return listData.get(temp);
    }
    ArrayList<MenuData> getListData(){
        return listData;
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;
        private TextView textView4;
        private MenuData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.user_title);
            textView2 = itemView.findViewById(R.id.user_content);
            textView3 = itemView.findViewById(R.id.menu_price);
            textView4 = itemView.findViewById(R.id.et_count);
        }

        void onBind(MenuData data) {
            this.data = data;

            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());
            textView3.setText(data.getPrice());
            textView4.setText(data.getCount());

            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
            textView3.setOnClickListener(this);
            textView4.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}