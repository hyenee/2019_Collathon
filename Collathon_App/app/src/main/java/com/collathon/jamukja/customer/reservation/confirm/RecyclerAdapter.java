package com.collathon.jamukja.customer.reservation.confirm;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder> {
    // adapter에 들어갈 list
    private ArrayList<Data> listData = new ArrayList<>();
    String current; //현재 시간 받아오는 변수
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

    public Object getItem(int position) {
        return listData.get(position) ;
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
            Log.i("TICKET CONFIRM", "addItem :" + listData.get(i).getId() + ", " + listData.get(i).getShop() + ", " + listData.get(i).getMenuCount() + ", " + listData.get(i).getTime());
        }
    }

    public String currentTime(){
        //현재 시간 가져오기
        TimeZone time;
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("HH");
        time = TimeZone.getTimeZone("Asia/Seoul");
        df.setTimeZone(time);
        Log.i("RESERVATION", "CURRENT TIME : "+ df.format(date));
        String current = df.format(date);
        return current;
    }


    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView shop, menu, time, table, table_textview;
        private Data data;
        private Button deleteButton, cant_reservation_delete_button;

        ItemViewHolder(View itemView) {
            super(itemView);

            shop = (TextView) itemView.findViewById(R.id.reservation_store_name);
            time = (TextView) itemView.findViewById(R.id.reservation_time);
            menu = (TextView) itemView.findViewById(R.id.reservation_menu);
            table = (TextView) itemView.findViewById(R.id.reservation_table);
            table_textview = (TextView) itemView.findViewById(R.id.reservation_table_textview);
            deleteButton = (Button) itemView.findViewById(R.id.reservation_delete_button);
            cant_reservation_delete_button = (Button) itemView.findViewById(R.id.cant_reservation_delete_button);
        }

        void onBind(Data data) {
            this.data = data;

            shop.setText(data.getShop());
            time.setText(data.getTime());
            menu.setText(data.getMenuCount());
            table.setText(data.getTable());

            current = currentTime(); //현재 시각 받아옴
            String temp = data.getTime().substring(0, 2); //예약 시간 중 앞 시간만 받아옴

            Log.i("CONFIRM RECYCLER", "temp"+ temp);
            if(Integer.parseInt(current) > Integer.parseInt(temp)-2){ // 예약 시간 1시간 전부터는 예약 취소 불가
                deleteButton.setVisibility(View.GONE);
                cant_reservation_delete_button.setVisibility(View.VISIBLE);
            }
            else
                cant_reservation_delete_button.setVisibility(View.GONE);

            if(data.getTable() == null || data.getTable().equals("")){
                table_textview.setVisibility(View.GONE);
                table.setVisibility(View.GONE);
            }

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("예약 취소")
                            .setMessage("예약을 취소하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteReservationAll();
                                }
                            })
                            .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .create()
                            .show();
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
            }catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }
}