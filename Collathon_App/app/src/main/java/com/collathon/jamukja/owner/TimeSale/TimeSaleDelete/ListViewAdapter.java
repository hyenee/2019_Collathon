package com.collathon.jamukja.owner.TimeSale.TimeSaleDelete;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.collathon.janolja.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final String TAG = "ListViewAdapter";
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<TimeSaleData> listViewItemList = new ArrayList<TimeSaleData>() ;
    private Context context;
    public static String name,count,price;
    public String saleTime;
    public int salePercent = 0;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }
    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.owner_time_sale_delete_item, parent, false);
        }
        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        TimeSaleData listViewItem = listViewItemList.get(position);

        TextView menuName = (TextView) convertView.findViewById(R.id.timesale_menuName) ;
        TextView salePrice = (TextView) convertView.findViewById(R.id.timesale_price) ;
        TextView menuCount = (TextView) convertView.findViewById(R.id.timesale_count) ;
        TextView saleTime  = (TextView) convertView.findViewById(R.id.timesale_time) ;

        menuName.setText(listViewItem.getMenuName());
        menuCount.setText(""+listViewItem.getMenuCount());
        saleTime.setText(listViewItem.getSaleTime());
        salePrice.setText(""+listViewItem.getSalePrice());

        return convertView;
    }


    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(TimeSaleData data ) {
        listViewItemList.add(data);
    }
}
