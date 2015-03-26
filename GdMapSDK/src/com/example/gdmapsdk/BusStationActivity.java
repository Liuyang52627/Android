package com.example.gdmapsdk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusStationItem;

public class BusStationActivity extends Activity {
	private BusLineItem lineItemst = null;// 公交线路搜索返回的busline
	private List<BusStationItem> busStationItems;
	private List<BusStationItem> busStationItemst;
	private String up,out;
	private ListView listView;
	private MyBusStationAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.busstation);
		listView = (ListView) findViewById(R.id.busstationlist);
		Intent intent = getIntent();
		lineItemst = Myapplion.getInstance().getBusLineItem();
		up=intent.getStringExtra("up");
		out=intent.getStringExtra("out");
		busStationItems=lineItemst.getBusStations();
		busStationItemst = new ArrayList<BusStationItem>();
		boolean isflag =false;
		for (int i = 0; i < busStationItems.size(); i++) {
			if(busStationItems.get(i).getBusStationName().equals(up)){
				isflag=true;
			}else if(busStationItems.get(i).getBusStationName().equals(out)){
				busStationItemst.add(busStationItems.get(i));
				isflag =false;
			}
			if (isflag) {
				busStationItemst.add(busStationItems.get(i));
			}
		}
		adapter = new MyBusStationAdapter(this, busStationItemst);
		listView.setAdapter(adapter);
	}
}
