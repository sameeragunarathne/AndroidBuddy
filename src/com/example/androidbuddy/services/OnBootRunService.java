package com.example.androidbuddy.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class OnBootRunService extends Service{

	@Override
	public IBinder onBind(Intent params) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d("messege", "boot service running");
	}
	
	
	
	

}
