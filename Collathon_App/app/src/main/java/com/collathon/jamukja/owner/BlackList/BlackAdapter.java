package com.collathon.jamukja.owner.BlackList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_black_list_list, parent, false);
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
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView clientID;
        private TextView count;
        private BlackData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            clientID = (TextView)itemView.findViewById(R.id.black_clientID);
            count= (TextView)itemView.findViewById(R.id.black_count);

        }

        void onBind(BlackData data) {
            this.data = data;

            Log.i("BlackAdapter", "clientIDTextView :"+data.getID());
            clientID.setText(data.getID());
            Log.i("BlackAdapter", "countTextView :"+data.getCount());
            count.setText(""+data.getCount()); // 숫자를 쓰면 android resource id로 인식

            //itemView.setOnClickListener(this);
            //clientIDTextView.setOnClickListener(this);
            //countTextView.setOnClickListener(this);

        }

//        @Override
//        public void onClick(View v) {
//            switch (v.getId()) {
//                case R.id.black_clientID:
//                    break;
//                case R.id.black_count:
//                    break;
//            }
//        }
    }
}
