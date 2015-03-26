package com.example.gdmapsdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.busline.BusLineItem;
import com.amap.api.services.busline.BusLineQuery;
import com.amap.api.services.busline.BusLineQuery.SearchType;
import com.amap.api.services.busline.BusLineResult;
import com.amap.api.services.busline.BusLineSearch;
import com.amap.api.services.busline.BusLineSearch.OnBusLineSearchListener;
import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.busline.BusStationQuery;
import com.amap.api.services.busline.BusStationResult;
import com.amap.api.services.busline.BusStationSearch;
import com.amap.api.services.busline.BusStationSearch.OnBusStationSearchListener;

public class CheckBusActivity extends Activity implements OnClickListener,OnBusStationSearchListener,OnBusLineSearchListener{
	private EditText up,out;
	private Button go;
	private ListView all;
	private List<BusLineItem>list;
	private BusLineResult busLineResult;
	private List<BusStationItem> stationItems;// ����վ���������ص�busStation
	private List<BusLineItem> lineItems = null;// ������·�������ص�busline
	private List<BusLineItem> lineItemst = null;// ������·�������ص�busline
	private BusLineQuery busLineQuery;// ������·��ѯ�Ĳ�ѯ��
	private BusLineSearch busLineSearch;// ������·�б��ѯ
	private List<BusStationItem> staItems;//����վ��
	private MycheckbusAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checkbus);
		init();
		all.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(CheckBusActivity.this,BusStationActivity.class);
				Myapplion.getInstance().setBusLineItem(lineItemst.get(arg2));
				intent.putExtra("up",up.getText().toString());
				intent.putExtra("out",out.getText().toString());
				startActivity(intent);
			}
		});
	}
	private void init() {
		up=(EditText) findViewById(R.id.up);
		go=(Button) findViewById(R.id.go);
		out=(EditText) findViewById(R.id.out);
		all=(ListView) findViewById(R.id.all);
		go.setOnClickListener(this);
	}
	@Override
	public void onClick(View arg0) {
		switch(arg0.getId()){
			case R.id.go:
				if(up.getText().toString().equals("")){
					showtoast("���������վ!");
				}else if(out.getText().toString().equals("")){
					showtoast("�������յ�վ!");
				}else{
					lineItemst = new ArrayList<BusLineItem>();
					adapter = new MycheckbusAdapter(lineItemst,this);
					all.setAdapter(adapter);
					if(up.getText().toString().equals(out.getText().toString())){
						showtoast("������յ���ͬһλ��!");
					}else{
						BusStationQuery query = new BusStationQuery(up.getText().toString(), "028");
						query.setPageSize(10);
						query.setPageNumber(0);
						BusStationSearch busStationSearch = new BusStationSearch(CheckBusActivity.this, query);
						busStationSearch.setOnBusStationSearchListener(CheckBusActivity.this);
						busStationSearch.searchBusStationAsyn();
					}
				}
				break;
		}
		
	}
	private void showtoast(String str){
		Toast.makeText(CheckBusActivity.this,str,1000).show();
	}
	@Override
	public void onBusStationSearched(BusStationResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPageCount() > 0
					&& result.getBusStations() != null
					&& result.getBusStations().size() > 0) {
				stationItems = result.getBusStations();
				lineItems = stationItems.get(0).getBusLineItems();
				for (int i = 0; i < lineItems.size(); i++) {
					String lineId = lineItems.get(i).getBusLineId();// �õ���ǰ���item������·id
					busLineQuery = new BusLineQuery(lineId,
							SearchType.BY_LINE_ID, "028");// ��һ��������ʾ������·id���ڶ���������ʾ������·id��ѯ��������������ʾ���ڳ��������߳�������
					BusLineSearch busLineSearch = new BusLineSearch(
							CheckBusActivity.this, busLineQuery);
					busLineSearch.setOnBusLineSearchListener(CheckBusActivity.this);
					busLineSearch.searchBusLineAsyn();// �첽��ѯ������·id
				}
			}
		}		
	}
	@Override
	public void onBusLineSearched(BusLineResult result, int rCode) {
		if (rCode == 0) {
			if(result != null && result.getQuery() != null
					&& result.getQuery().equals(busLineQuery)){
				
			}else if (result.getQuery().getCategory() == SearchType.BY_LINE_ID) {//��������վ
				busLineResult = result;
				staItems = busLineResult.getBusLines().get(0)
						.getBusStations();
				String upt = "";
				for (int i = 0; i < staItems.size(); i++) {
					if(staItems.get(i).getBusStationName().equals(up.getText().toString())){
						upt=staItems.get(i).getBusStationName();
					}
					if(staItems.get(i).getBusStationName().equals(out.getText().toString())&&!upt.equals("")){
						lineItemst.add(result.getBusLines().get(0));
					}
				}
				adapter.notifyDataSetChanged();
			}
		}
	}
}
