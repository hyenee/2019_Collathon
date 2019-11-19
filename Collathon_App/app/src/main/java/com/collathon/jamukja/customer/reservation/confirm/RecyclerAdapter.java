package com.collathon.jamukja.customer.reservation.confirm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.MainActivity;
import com.collathon.jamukja.NetworkManager;
import com.collathon.janolja.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_reservation_confirm_view, parent, false);
        this.context = parent.getContext();
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

//    public Object getItem(int position) {
//        return listData.get(position) ;
//    }


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
        private Button deleteButton;

        ItemViewHolder(View itemView) {
            super(itemView);

            shop = (TextView) itemView.findViewById(R.id.reservation_store_name);
           // menu = (TextView) itemView.findViewById(R.id.reservation_ticket_menu);
           // count = (TextView) itemView.findViewById(R.id.reservation_ticket_menu_count);
            time = (TextView) itemView.findViewById(R.id.reservation_time);
            menu = (TextView) itemView.findViewById(R.id.reservation_menu);
            deleteButton = (Button) itemView.findViewById(R.id.reservation_delete_button);
        }

        void onBind(Data data) {
            this.data = data;

            shop.setText(data.getShop());
            //menu.setText(data.getMenu());
            //count.setText(data.getCount());
            time.setText(data.getTime());
            menu.setText(data.getMenu());

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteReservationAll();

                }
            });
        }


        public void deleteReservationAll(){
            try {
                NetworkManager nm = new NetworkManager();
                String client_site = "/reservation/delete?reservation="+data.getId();
                Log.i("DELETE RESERVATION", "SITE= "+client_site);
                nm.postInfo(client_site, "POST");

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("DELETE RESERVATION", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("DELETE RESERVATION", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("예약 취소 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("예약 취소 성공")
                            .create()
                            .show();
                    Intent intent = new Intent(context, ReservationConfirmActivity.class);
                    intent.putExtra("userID", userID);
                    itemView.getContext().startActivity(intent);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
