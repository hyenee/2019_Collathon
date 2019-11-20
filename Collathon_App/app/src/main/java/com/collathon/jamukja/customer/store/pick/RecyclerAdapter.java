package com.collathon.jamukja.customer.store.pick;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.customer.store.category.detail.StoreDetailListActivity;
import com.collathon.janolja.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    public String userID;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_pick_store_view, parent, false);
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
            Log.i("PICK", "addItem :"+ listData.get(i).getShop()+", "+listData.get(i).getTel()+", "+listData.get(i).getAddress()
            +", "+ listData.get(i).getCategory());
        }
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{ // implements View.OnClickListener

        private TextView pick_store_name, pick_category, pick_tel, pick_address;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            pick_store_name = itemView.findViewById(R.id.pick_store_name);
            pick_category = itemView.findViewById(R.id.pick_category);
            pick_tel = itemView.findViewById(R.id.pick_tel);
            pick_address = itemView.findViewById(R.id.pick_address);
        }

        void onBind(Data data) {
            this.data = data;
            pick_store_name.setText(data.getShop());
            pick_category.setText(data.getCategory());
            pick_tel.setText(data.getTel());
            pick_address.setText(data.getAddress());

            //itemView.setOnClickListener(this);
            //pick_store_name.setOnClickListener(this);
            //pick_tel.setOnClickListener(this);
            //pick_address.setOnClickListener(this);
        }

/*
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pick_store_name:
                    Toast.makeText(context, data.getShop(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), StoreDetailListActivity.class);
                    intent.putExtra("userID", userID);
                    intent.putExtra("shopName", data.getShop());
                    intent.putExtra("shopID", data.getShop_id());
                    v.getContext().startActivity(intent);
                    break;

                case R.id.pick_tel:
                    Toast.makeText(context, data.getShop(), Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(v.getContext(), StoreDetailListActivity.class);
                    intent1.putExtra("userID", userID);
                    intent1.putExtra("shopName", data.getShop());
                    intent1.putExtra("shopID", data.getShop_id());
                    v.getContext().startActivity(intent1);
                    break;

                case R.id.pick_address:
                    Toast.makeText(context, data.getShop(), Toast.LENGTH_SHORT).show();
                    Intent intent2 = new Intent(v.getContext(), StoreDetailListActivity.class);
                    intent2.putExtra("userID", userID);
                    intent2.putExtra("shopName", data.getShop());
                    intent2.putExtra("shopID", data.getShop_id());
                    v.getContext().startActivity(intent2);
                    break;
            }
        }


 */
    }
}