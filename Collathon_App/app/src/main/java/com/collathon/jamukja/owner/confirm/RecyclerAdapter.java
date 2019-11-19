package com.collathon.jamukja.owner.confirm;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.jamukja.NetworkManager;
import com.collathon.jamukja.customer.reservation.confirm.ReservationConfirmActivity;
import com.collathon.jamukja.owner.confirm.Data;
import com.collathon.janolja.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    private Context context;
    public String shopID;
    private String comment = "bad";

    @NonNull
    @Override
    public RecyclerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_list_seat, parent, false);
        this.context = parent.getContext();
        return new RecyclerAdapter.ItemViewHolder(view);
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

        private TextView user;
        private TextView menu;
        private TextView count;
        private TextView time;
        private Data data;
        private Button deleteButton, addBlackButton;

        ItemViewHolder(View itemView) {
            super(itemView);

            user = (TextView) itemView.findViewById(R.id.owner_reservation_id);
            // menu = (TextView) itemView.findViewById(R.id.reservation_ticket_menu);
            // count = (TextView) itemView.findViewById(R.id.reservation_ticket_menu_count);
            time = (TextView) itemView.findViewById(R.id.owner_reservation_time);
            menu = (TextView) itemView.findViewById(R.id.owner_reservation_menu);
            deleteButton = (Button) itemView.findViewById(R.id.btn_owner_reservation_delete);
            addBlackButton = (Button) itemView.findViewById(R.id.btn_owner_add_blacklist);
        }

        void onBind(Data data) {
            this.data = data;

            final String userID = data.getShop();
            user.setText(data.getShop());
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

            addBlackButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    addBlackList(shopID, userID);
                }
            });
        }

        public void addBlackList(String shopID, String userID){
            ///blacklist/add?id={client_id}&shop={shop_id}&comment={comment}
            try {
                NetworkManager nm = new NetworkManager();
                String site = "/blacklist/add?id="+userID+"&shop="+shopID+"&comment="+comment;
                Log.i("ADD BLACKLIST", "SITE= "+site);
                nm.postInfo(site, "POST");

                while(true){ // thread 작업이 끝날 때까지 대기
                    if(nm.isEnd){
                        break;
                    }
                    Log.i("ADD BLACKLIST", "아직 작업 안끝남.");
                }
                JSONObject jsonObject = nm.getResult();
                String success = jsonObject.getString("result");
                Log.i("ADD BLACKLIST", "서버에서 받아온 result = " + success);

                if (success.equals("ERROR")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("블랙리스트 등록 실패")
                            .setNegativeButton("다시 시도", null)
                            .create()
                            .show();
                }

                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setMessage("블랙리스트 등록 성공")
                            .setPositiveButton("확인", null)
                            .create()
                            .show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
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

                    Intent intent = new Intent(context, Owner_Reservation_View.class);
                    intent.putExtra("shopID", shopID);
                    itemView.getContext().startActivity(intent);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }
}
