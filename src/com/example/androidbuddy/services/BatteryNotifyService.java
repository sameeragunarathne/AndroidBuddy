package com.example.androidbuddy.services;

import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.BatteryLowMesseges;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.CollectionResponseBatteryLowMesseges;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

public class BatteryNotifyService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		new BatteryLevelHandleAsyncTask().execute();
	}

	private class BatteryLevelHandleAsyncTask extends
			AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			List<BatteryLowMesseges> list = null;

			try {
				Userrelateddataendpoint.Builder endpointbuilder = new Userrelateddataendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				endpointbuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataendpoint entityservice = endpointbuilder.build();

				CollectionResponseBatteryLowMesseges batteryLowmasseges = entityservice
						.listBatteryLowMesseges().execute();
				list = batteryLowmasseges.getItems();

				if (list != null) {
					for (BatteryLowMesseges batteryLowMesseges : list) {
						SmsManager sms = SmsManager.getDefault();
						sms.sendTextMessage(batteryLowMesseges.getNumber(),
								null, batteryLowMesseges.getMessege(), null,
								null);
					}
				}

			} catch (Exception e) {

			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {

		}

	}

}
