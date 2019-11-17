package com.collathon.jamukja.customer.store.category.detail;

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

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>{
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_store_detail_menu_view, parent, false);
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
            Log.i("MENU", "addItem :"+ listData.get(i).getSale()+", "+ listData.get(i).getName()+", "+ listData.get(i).getPrice()+", "+ listData.get(i).getDescription());
        }
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder{

        private TextView name;
        private TextView price;
        private TextView description;
        private ImageView timesale;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            name = (TextView)itemView.findViewById(R.id.menu_name);
            price = (TextView)itemView.findViewById(R.id.menu_price);
            description = (TextView)itemView.findViewById(R.id.menu_description);
            timesale = (ImageView)itemView.findViewById(R.id.timesale);
        }

        void onBind(Data data) {
            this.data = data;

            name.setText(data.getName());
            price.setText(data.getPrice());
            description.setText(data.getDescription());


            if(data.getSale().equals("Y")){
                timesale.setVisibility(View.VISIBLE);
            }
            else if(data.getSale().equals("N"))
                timesale.setVisibility(View.INVISIBLE);


        }


    }
}
