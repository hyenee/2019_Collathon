package com.collathon.jamukja.owner;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.janolja.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<Data> listData = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private TextView textView3, textView_book;
    private ImageView imageView;
    private MenuData data;
    private Context context;



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_item_view_store, parent, false);
        View view1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_main,parent,false);

        textView3 = view1.findViewById(R.id.user_name);
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

    public void addItem(Data data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }
    public Data getData(int index){
        return listData.get(index);
    }
    void deldete(int str){
        Log.v("삭제", String.valueOf(listData.size()));
        listData.remove(str);

    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private TextView textView3;

        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.user_title);

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

                case R.id.user_title:
                    Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), Store_info.class);
                    intent.putExtra("title",data.getTitle().toString());
                    intent.putExtra("owner_ID",data.getOwner().toString());
                    intent.putExtra("shop_id",data.getId().toString());

                    v.getContext().startActivity(intent);
                    break;

            }
        }
    }
}