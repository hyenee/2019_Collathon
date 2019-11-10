package com.collathon.jamukja.store.category.rice_1;

import android.content.Context;
import android.content.Intent;
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
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private ImageView imageView;
    private Data data;
    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_store_item_view_rice, parent, false);
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
    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.store_item_view_rice_title);
            textView2 = itemView.findViewById(R.id.store_item_view_rice_content);
        }

        void onBind(Data data) {
            this.data = data;

            textView1.setText(data.getTitle());
            textView2.setText(data.getContent());

            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.store_item_view_rice_title:
                    Toast.makeText(context, data.getTitle(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(v.getContext(), RiceStoreListActivity.class);
                    v.getContext().startActivity(intent);
                    break;
                case R.id.store_item_view_rice_content:
                    Toast.makeText(context, data.getContent(), Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(v.getContext(), RiceStoreListActivity.class);
                    v.getContext().startActivity(intent1);
                    break;


            }

        }
    }
}
