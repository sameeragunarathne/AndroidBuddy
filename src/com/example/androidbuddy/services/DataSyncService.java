package com.example.androidbuddy.services;

import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.CallLogCollection;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog;


import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;

public class DataSyncService extends Service {

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new dataSyncAsyncTask().execute("menda91");
	}

	private class dataSyncAsyncTask extends AsyncTask<String, Void, Void> {

		

		@Override
		protected Void doInBackground(String... params) {
			
			List<CallLog> list; 
			try{
				Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(), new GsonFactory(),
						null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = servicebuilder.build();
				
				CallLogCollection collection=service.listCallLogByUser(params[0]).execute();
				
				
				}catch(Exception e){
					
				}
				
			
			return null;
		}

	}

}
