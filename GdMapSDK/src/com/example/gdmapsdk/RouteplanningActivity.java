package com.example.gdmapsdk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.services.busline.BusStationItem;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.amap.api.services.route.BusPath;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.BusStep;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RouteBusLineItem;
import com.amap.api.services.route.RouteBusWalkItem;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.RouteSearch.BusRouteQuery;
import com.amap.api.services.route.RouteSearch.OnRouteSearchListener;
import com.amap.api.services.route.WalkRouteResult;
import com.amap.api.services.route.WalkStep;

public class RouteplanningActivity extends Activity implements
		OnPoiSearchListener,OnRouteSearchListener,OnClickListener{
	private ListView lView;
	private EditText start, stop;
	private BusRouteResult busRouteResult;// 公交模式查询结果
	private LatLonPoint startPoint = null;// 起点坐标
	private LatLonPoint endPoint = null;// 终点坐标
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private RouteSearch routeSearch;
	private int busMode = RouteSearch.BusDefault;// 公交默认模式
	private Button go;
	private List<String>path;
	private MyRouAdapter adapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeplanning);
		routeSearch = new RouteSearch(RouteplanningActivity.this);
		routeSearch.setRouteSearchListener(this);
		init();
	}

	private void init() {
		lView = (ListView) findViewById(R.id.routelist);
		start = (EditText) findViewById(R.id.start);
		stop = (EditText) findViewById(R.id.stop);
		go = (Button) findViewById(R.id.go);
		go.setOnClickListener(this);
	}

	private void check() {
		if (start.getText().toString().equals("")) {
			showtoast("请输入起点");
		} else if (stop.getText().toString().equals("")) {
			showtoast("请输入终点");
		} else if (stop.getText().toString().equals(start.getText().toString())) {
			showtoast("起点与终点距离很近，您可以步行前往");
		} else {
			startSearchQuery = new PoiSearch.Query(start.getText().toString()
					.trim(), "", "028"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
			startSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
			startSearchQuery.setPageSize(20);// 设置每页返回多少条数据
			PoiSearch poiSearch = new PoiSearch(RouteplanningActivity.this,
					startSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();// 异步poi查询
		}
	}

	private void showtoast(String str) {
		Toast.makeText(RouteplanningActivity.this, str, 1000).show();
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int rCode) {

	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		if (rCode == 0) {// 返回成功
			if (result != null && result.getQuery() != null
					&& result.getPois() != null && result.getPois().size() > 0) {// 搜索poi的结果
				if (result.getQuery().equals(startSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// 取得poiitem数据
					startPoint = poiItems.get(0).getLatLonPoint();
					endSearchQuery = new PoiSearch.Query(stop.getText().toString()
							.trim(), "", "028"); // 第一个参数表示查询关键字，第二参数表示poi搜索类型，第三个参数表示城市区号或者城市名
					endSearchQuery.setPageNum(0);// 设置查询第几页，第一页从0开始
					endSearchQuery.setPageSize(20);// 设置每页返回多少条数据

					PoiSearch poiSearch = new PoiSearch(RouteplanningActivity.this,
							endSearchQuery);
					poiSearch.setOnPoiSearchListener(this);
					poiSearch.searchPOIAsyn(); // 异步poi查询
				} else if (result.getQuery().equals(endSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();
					endPoint = poiItems.get(0).getLatLonPoint();
					searchRouteResult(startPoint,endPoint);
				}
			}
		}
	}
	/**
	 * 开始搜索路径规划方案
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		
		BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "成都", 0);// 第一个参数表示路径规划的起点和终点，第二个参数表示公交查询模式，第三个参数表示公交查询城市区号，第四个参数表示是否计算夜班车，0表示不计算
		
		routeSearch.calculateBusRouteAsyn(query);// 异步路径规划公交模式查询
	}

	@Override
	public void onClick(View arg0) {
		check();
	}

	@Override
	public void onBusRouteSearched(BusRouteResult result, int rCode) {
		
		if (rCode == 0) {
			if (result != null && result.getPaths() != null
					&& result.getPaths().size() > 0) {
				busRouteResult = result;
				BusPath busPath = busRouteResult.getPaths().get(0);
				List<BusStep>list=busPath.getSteps();
				path = new ArrayList<String>();
				for (int i = 0; i < list.size(); i++) {
					RouteBusWalkItem item = list.get(i).getWalk();
					 List<WalkStep> walkStep=item.getSteps();
					 for (int j = 0; j < walkStep.size(); j++) {
						System.out.println(walkStep.get(j).getInstruction());
						path.add(walkStep.get(j).getInstruction());
					}
					 RouteBusLineItem buslineitem =list.get(i).getBusLine();
					 if(buslineitem!=null){
						 path.add("乘坐"+buslineitem.getBusLineName());
						 path.add("在"+buslineitem.getDepartureBusStation().getBusStationName()+"站上车");
						 System.out.println("乘坐"+buslineitem.getBusLineName());
						 System.out.println("在"+buslineitem.getDepartureBusStation().getBusStationName()+"站上车");
						 List<BusStationItem> stationItem = buslineitem.getPassStations();
						 for (int j = 0; j < stationItem.size(); j++) {
							System.out.println(stationItem.get(j).getBusStationName());
							path.add(stationItem.get(j).getBusStationName());
						}
						 System.out.println("到"+buslineitem.getArrivalBusStation().getBusStationName()+"下车");
						 path.add("到"+buslineitem.getArrivalBusStation().getBusStationName()+"下车");
					 }
				}
				adapter = new MyRouAdapter(path, RouteplanningActivity.this);
				lView.setAdapter(adapter);
			}
		}
		
	}

	@Override
	public void onDriveRouteSearched(DriveRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onWalkRouteSearched(WalkRouteResult arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


}
