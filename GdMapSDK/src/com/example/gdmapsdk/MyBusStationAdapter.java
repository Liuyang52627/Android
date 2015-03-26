package com.example.gdmapsdk;

import java.util.List;

import com.amap.api.services.busline.BusStationItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBusStationAdapter extends BaseAdapter {
	private Context context;
	private List<BusStationItem> busStationItems;
	public MyBusStationAdapter(Context context,List<BusStationItem> busStationItems){
		this.context=context;
		this.busStationItems = busStationItems;
	};
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return busStationItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return busStationItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		view = LayoutInflater.from(context).inflate(R.layout.list_itme,null);
		TextView item_name = (TextView) view.findViewById(R.id.item_name);
		item_name.setText(busStationItems.get(arg0).getBusStationName());
		return view;
	}

}
