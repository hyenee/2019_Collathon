package com.collathon.jamukja.owner.Seat;



import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.collathon.janolja.R;

import java.util.ArrayList;

public class ManagerAdapter extends RecyclerView.Adapter<ManagerAdapter.ItemViewHolder>  {
    private ArrayList<BookData> listData = new ArrayList<>();
    private TextView textView1;
    private TextView textView2;
    private BookData data;
    private Context context;

    @NonNull
    @Override
    public ManagerAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // LayoutInflater를 이용하여 전 단계에서 만들었던 item.xml을 inflate 시킵니다.
        // return 인자는 ViewHolder 입니다.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.owner_list_seat_list, parent, false);
        this.context = parent.getContext();
        return new ManagerAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ManagerAdapter.ItemViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    void addItem(BookData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }




    void deldete(int str){
        listData.remove(str);
    }
    BookData getData(int temp){
        return listData.get(temp);
    }
    ArrayList<BookData> getListData(){
        return listData;
    }



    // RecyclerView의 핵심인 ViewHolder 입니다.
    // 여기서 subView를 setting 해줍니다.
    class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView textView1;
        private TextView textView2;
        private BookData data;

        ItemViewHolder(View itemView) {
            super(itemView);

            textView1 = itemView.findViewById(R.id.et_book_menu);
            textView2 = itemView.findViewById(R.id.et_count);
        }

        void onBind(BookData data) {
            this.data = data;

            textView1.setText(data.getTable());
            textView2.setText(data.getCount());

            itemView.setOnClickListener(this);
            textView1.setOnClickListener(this);
            textView2.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}