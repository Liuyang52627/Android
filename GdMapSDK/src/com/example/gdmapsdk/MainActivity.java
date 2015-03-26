package com.example.gdmapsdk;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

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

public class MainActivity extends Activity implements OnBusLineSearchListener,
		OnBusStationSearchListener, OnClickListener {
	private ListView Bus;
	private EditText Busname;
	private Button button;
	private BusLineResult busLineResult;
	private List<BusLineItem> lineItems = null;// ������·�������ص�busline
	private BusLineQuery busLineQuery;// ������·��ѯ�Ĳ�ѯ��
	private List<BusStationItem> staItems;
	private BusStationResult busStationResult;// ����վ���������صĽ��
	private List<BusStationItem> stationItems;// ����վ���������ص�busStation
	private BusStationQuery busStationQuery;// ����վ���ѯ�Ĳ�ѯ��
	private BusLineSearch busLineSearch;// ������·�б��ѯ
	private MyBusAdapter adapter;
	private String type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		Bus.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				if (type.equals("BusLineItem")) {
					String lineId = lineItems.get(pos).getBusLineId();// �õ���ǰ���item������·id
					busLineQuery = new BusLineQuery(lineId,
							SearchType.BY_LINE_ID, "028");// ��һ��������ʾ������·id���ڶ���������ʾ������·id��ѯ��������������ʾ���ڳ��������߳�������
					BusLineSearch busLineSearch = new BusLineSearch(
							MainActivity.this, busLineQuery);
					busLineSearch.setOnBusLineSearchListener(MainActivity.this);
					busLineSearch.searchBusLineAsyn();// �첽��ѯ������·id
				}else{
					BusStationQuery query = new BusStationQuery(staItems.get(pos)
							.getBusStationName(), "028");
					query.setPageSize(10);
					query.setPageNumber(0);
					BusStationSearch busStationSearch = new BusStationSearch(MainActivity.this, query);
					busStationSearch.setOnBusStationSearchListener(MainActivity.this);
					busStationSearch.searchBusStationAsyn();
				}
			}
		});
	}

	private void init() {
		Bus = (ListView) findViewById(R.id.Bus);
		Busname = (EditText) findViewById(R.id.Busname);
		button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void check() {
		busLineQuery = new BusLineQuery(Busname.getText().toString(),
				SearchType.BY_LINE_NAME, "028");// ��һ��������ʾ������·�����ڶ���������ʾ������·��ѯ��������������ʾ���ڳ��������߳�������
		busLineSearch = new BusLineSearch(this, busLineQuery);// ��������
		busLineSearch.setOnBusLineSearchListener(this);// ���ò�ѯ����ļ���
		busLineSearch.searchBusLineAsyn();// �첽��ѯ������·����
	}

	@Override
	public void onBusLineSearched(BusLineResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null
					&& result.getQuery().equals(busLineQuery)) {
				if (result.getQuery().getCategory() == SearchType.BY_LINE_NAME) {
					if (result.getPageCount() > 0
							&& result.getBusLines() != null
							&& result.getBusLines().size() > 0) {//����������
						busLineResult = result;
						lineItems = result.getBusLines();
						type = "BusLineItem";
						Bus.setAdapter(new MyBusAdapter(MainActivity.this,
								lineItems, type));
					}else{
						BusStationQuery query = new BusStationQuery(Busname.getText().toString(), "028");
						query.setPageSize(10);
						query.setPageNumber(0);
						BusStationSearch busStationSearch = new BusStationSearch(MainActivity.this, query);
						busStationSearch.setOnBusStationSearchListener(MainActivity.this);
						busStationSearch.searchBusStationAsyn();
					}

				} else if (result.getQuery().getCategory() == SearchType.BY_LINE_ID) {//��������վ
					busLineResult = result;
					staItems = busLineResult.getBusLines().get(0)
							.getBusStations();
					type = "BusStationItem";
					Bus.setAdapter(new MyBusAdapter(MainActivity.this,
							staItems, type));
					
				}
			}
		}
	}

	/**
	 * ����վ���ѯ����ص�
	 */
	public void onBusStationSearched(BusStationResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getPageCount() > 0
					&& result.getBusStations() != null
					&& result.getBusStations().size() > 0) {
				busStationResult = result;
				stationItems = result.getBusStations();
				lineItems = stationItems.get(0).getBusLineItems();
				type="BusLineItem";
				Bus.setAdapter(new MyBusAdapter(MainActivity.this,lineItems,type));
			}
		}
	}

	@Override
	public void onClick(View arg0) {
		check();
	}

}
