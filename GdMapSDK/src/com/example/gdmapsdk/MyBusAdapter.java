package com.example.gdmapsdk;

import java.util.List;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyBusAdapter extends BaseAdapter {
	private Context context;
	private List lineItems;
	private String type;
	public MyBusAdapter(Context context,List lineItems,String type) {
		this.context=context;
		this.type = type;
		this.lineItems=lineItems;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lineItems.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lineItems.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		view = LayoutInflater.from(context).inflate(R.layout.list_itme,null);
		TextView name = (TextView) view.findViewById(R.id.item_name);
		if (type.equals("BusLineItem")) {
			name.setText(((BusLineItem)lineItems.get(arg0)).getBusLineName());
		}else if(type.equals("BusStationItem")){
			name.setText(((BusStationItem)lineItems.get(arg0)).getBusStationName());
		}
		return view;
	}

}
