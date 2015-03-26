package com.example.gdmapsdk;


import android.app.Application;

import com.amap.api.services.busline.BusLineItem;

public class Myapplion extends Application {
	private static Myapplion instance;
	private BusLineItem busLineItem;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		instance=this;
	}
	public static Myapplion getInstance(){
		if(instance==null){
			instance =new Myapplion();
		}
		return instance;
	}
	public BusLineItem getBusLineItem(){
		return busLineItem;
	}
	public void setBusLineItem(BusLineItem busLineItem){
		this.busLineItem = busLineItem;
	}
}
