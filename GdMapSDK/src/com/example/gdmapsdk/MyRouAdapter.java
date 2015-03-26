package com.example.gdmapsdk;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyRouAdapter extends BaseAdapter {
	private List<String>list;
	private Context context;
	public MyRouAdapter(List<String>list,Context context) {
		this.context=context;
		this.list=list;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
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
		name.setText(list.get(arg0));
		return view;
	}

}
