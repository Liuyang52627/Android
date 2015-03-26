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
	private BusRouteResult busRouteResult;// ����ģʽ��ѯ���
	private LatLonPoint startPoint = null;// �������
	private LatLonPoint endPoint = null;// �յ�����
	private PoiSearch.Query startSearchQuery;
	private PoiSearch.Query endSearchQuery;
	private RouteSearch routeSearch;
	private int busMode = RouteSearch.BusDefault;// ����Ĭ��ģʽ
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
			showtoast("���������");
		} else if (stop.getText().toString().equals("")) {
			showtoast("�������յ�");
		} else if (stop.getText().toString().equals(start.getText().toString())) {
			showtoast("������յ����ܽ��������Բ���ǰ��");
		} else {
			startSearchQuery = new PoiSearch.Query(start.getText().toString()
					.trim(), "", "028"); // ��һ��������ʾ��ѯ�ؼ��֣��ڶ�������ʾpoi�������ͣ�������������ʾ�������Ż��߳�����
			startSearchQuery.setPageNum(0);// ���ò�ѯ�ڼ�ҳ����һҳ��0��ʼ
			startSearchQuery.setPageSize(20);// ����ÿҳ���ض���������
			PoiSearch poiSearch = new PoiSearch(RouteplanningActivity.this,
					startSearchQuery);
			poiSearch.setOnPoiSearchListener(this);
			poiSearch.searchPOIAsyn();// �첽poi��ѯ
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
		if (rCode == 0) {// ���سɹ�
			if (result != null && result.getQuery() != null
					&& result.getPois() != null && result.getPois().size() > 0) {// ����poi�Ľ��
				if (result.getQuery().equals(startSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();// ȡ��poiitem����
					startPoint = poiItems.get(0).getLatLonPoint();
					endSearchQuery = new PoiSearch.Query(stop.getText().toString()
							.trim(), "", "028"); // ��һ��������ʾ��ѯ�ؼ��֣��ڶ�������ʾpoi�������ͣ�������������ʾ�������Ż��߳�����
					endSearchQuery.setPageNum(0);// ���ò�ѯ�ڼ�ҳ����һҳ��0��ʼ
					endSearchQuery.setPageSize(20);// ����ÿҳ���ض���������

					PoiSearch poiSearch = new PoiSearch(RouteplanningActivity.this,
							endSearchQuery);
					poiSearch.setOnPoiSearchListener(this);
					poiSearch.searchPOIAsyn(); // �첽poi��ѯ
				} else if (result.getQuery().equals(endSearchQuery)) {
					List<PoiItem> poiItems = result.getPois();
					endPoint = poiItems.get(0).getLatLonPoint();
					searchRouteResult(startPoint,endPoint);
				}
			}
		}
	}
	/**
	 * ��ʼ����·���滮����
	 */
	public void searchRouteResult(LatLonPoint startPoint, LatLonPoint endPoint) {
		RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(
				startPoint, endPoint);
		
		BusRouteQuery query = new BusRouteQuery(fromAndTo, busMode, "�ɶ�", 0);// ��һ��������ʾ·���滮�������յ㣬�ڶ���������ʾ������ѯģʽ��������������ʾ������ѯ�������ţ����ĸ�������ʾ�Ƿ����ҹ�೵��0��ʾ������
		
		routeSearch.calculateBusRouteAsyn(query);// �첽·���滮����ģʽ��ѯ
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
						 path.add("����"+buslineitem.getBusLineName());
						 path.add("��"+buslineitem.getDepartureBusStation().getBusStationName()+"վ�ϳ�");
						 System.out.println("����"+buslineitem.getBusLineName());
						 System.out.println("��"+buslineitem.getDepartureBusStation().getBusStationName()+"վ�ϳ�");
						 List<BusStationItem> stationItem = buslineitem.getPassStations();
						 for (int j = 0; j < stationItem.size(); j++) {
							System.out.println(stationItem.get(j).getBusStationName());
							path.add(stationItem.get(j).getBusStationName());
						}
						 System.out.println("��"+buslineitem.getArrivalBusStation().getBusStationName()+"�³�");
						 path.add("��"+buslineitem.getArrivalBusStation().getBusStationName()+"�³�");
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
