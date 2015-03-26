package com.example.gdmapsdk;

import java.util.List;

import com.amap.api.services.busline.BusLineItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MycheckbusAdapter extends BaseAdapter {
	private Context context;
	private List<BusLineItem> lineItemst;
	public MycheckbusAdapter(List<BusLineItem> lineItemst,Context context) {
		this.lineItemst=lineItemst;
		this.context = context;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lineItemst.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lineItemst.get(arg0);
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
		item_name.setText(lineItemst.get(arg0).getBusLineName());
		return view;
	}

}
