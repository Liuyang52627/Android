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
	private List<BusLineItem> lineItems = null;// 公交线路搜索返回的busline
	private BusLineQuery busLineQuery;// 公交线路查询的查询类
	private List<BusStationItem> staItems;
	private BusStationResult busStationResult;// 公交站点搜索返回的结果
	private List<BusStationItem> stationItems;// 公交站点搜索返回的busStation
	private BusStationQuery busStationQuery;// 公交站点查询的查询类
	private BusLineSearch busLineSearch;// 公交线路列表查询
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
					String lineId = lineItems.get(pos).getBusLineId();// 得到当前点击item公交线路id
					busLineQuery = new BusLineQuery(lineId,
							SearchType.BY_LINE_ID, "028");// 第一个参数表示公交线路id，第二个参数表示公交线路id查询，第三个参数表示所在城市名或者城市区号
					BusLineSearch busLineSearch = new BusLineSearch(
							MainActivity.this, busLineQuery);
					busLineSearch.setOnBusLineSearchListener(MainActivity.this);
					busLineSearch.searchBusLineAsyn();// 异步查询公交线路id
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
				SearchType.BY_LINE_NAME, "028");// 第一个参数表示公交线路名，第二个参数表示公交线路查询，第三个参数表示所在城市名或者城市区号
		busLineSearch = new BusLineSearch(this, busLineQuery);// 设置条件
		busLineSearch.setOnBusLineSearchListener(this);// 设置查询结果的监听
		busLineSearch.searchBusLineAsyn();// 异步查询公交线路名称
	}

	@Override
	public void onBusLineSearched(BusLineResult result, int rCode) {
		if (rCode == 0) {
			if (result != null && result.getQuery() != null
					&& result.getQuery().equals(busLineQuery)) {
				if (result.getQuery().getCategory() == SearchType.BY_LINE_NAME) {
					if (result.getPageCount() > 0
							&& result.getBusLines() != null
							&& result.getBusLines().size() > 0) {//搜索公交车
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

				} else if (result.getQuery().getCategory() == SearchType.BY_LINE_ID) {//搜索公交站
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
	 * 公交站点查询结果回调
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
