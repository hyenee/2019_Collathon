package com.collathon.jamukja.owner.BlackList;

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

public class BlackAdapter extends RecyclerView.Adapter<BlackAdapter.ItemViewHolder> {

    // adapter에 들어갈 list 입니다.
    private ArrayList<BlackData> listData = new ArrayList<>();
    private Context context;



    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_item_view_store, parent, false);
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

    void addItem(BlackData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
        for(int i=0; i<listData.size(); i++){
            Log.i("BlackAdapter", "addItem :"+
                    listData.get(i).getID()+", "+ listData.get(i).getCount());
        }
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView clientIDTextView;
        private TextView countTextView;
        private BlackData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            clientIDTextView = itemView.findViewById(R.id.black_clientID);
            countTextView = itemView.findViewById(R.id.black_count);

        }

        void onBind(BlackData data) {
            this.data = data;

            clientIDTextView.setText(data.getID());
            Log.i("BlackAdapter", "clientIDTextView :"+data.getID());
            countTextView.setText(""+data.getCount()); // 숫자를 쓰면 android resource id로 인식
            Log.i("BlackAdapter", "countTextView :"+data.getCount());

            itemView.setOnClickListener(this);
            //clientIDTextView.setOnClickListener(this);
            //countTextView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.user_title:
                    break;
                case R.id.user_content:
                    break;
            }
        }
    }
}
