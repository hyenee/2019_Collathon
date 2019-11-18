package com.collathon.jamukja.customer.reservation.ticket_confirm;

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
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();

    private Context context;

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation_ticket_confirm_view, parent, false);
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
        for (int i = 0; i < listData.size(); i++) {
           Log.i("TICKET CONFIRM", "addItem :" + listData.get(i).getId() + ", " + listData.get(i).getShop() + ", " + listData.get(i).getMenu() + ", " + listData.get(i).getTime());
            //Log.i("TICKET CONFIRM", "addItem :" + listData.get(i).getId() + ", " + listData.get(i).getShop() + ", " + listData.get(i).getOrderList() + ", " + listData.get(i).getTime());
        }
    }

    public void check(List<Data> listData){

    }

    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView shop;
        private TextView menu;
        private TextView count;
        private TextView time;
        private Data data;

        ItemViewHolder(View itemView) {
            super(itemView);

            shop = (TextView) itemView.findViewById(R.id.reservation_store_name);
           // menu = (TextView) itemView.findViewById(R.id.reservation_ticket_menu);
           // count = (TextView) itemView.findViewById(R.id.reservation_ticket_menu_count);
            time = (TextView) itemView.findViewById(R.id.reservation_time);
            menu = (TextView) itemView.findViewById(R.id.reservation_menu);
        }

        void onBind(Data data) {
            this.data = data;

            shop.setText(data.getShop());
            //menu.setText(data.getMenu());
            //count.setText(data.getCount());
            time.setText(data.getTime());
            menu.setText(data.getMenu());
        }

    }
}
