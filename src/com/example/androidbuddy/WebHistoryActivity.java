package com.example.androidbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.BrowserHistoryItem;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.BrowserHistoryItemCollection;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

public class WebHistoryActivity extends ListActivity{

	private ProgressDialog pDialog;

	private static final String TAG_INDEX = "index";
	private static final String TAG_LINK = "link";
	
	ArrayList<HashMap<String, String>> webList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.web_history);
		
		webList = new ArrayList<HashMap<String, String>>();
	}
	
	private class LoadInboxAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(WebHistoryActivity.this);
			pDialog.setMessage("Loading Inbox...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			List<BrowserHistoryItem> historylog = null;
			try {
				Userrelateddataendpoint.Builder servicebuilder = new Userrelateddataendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataendpoint service = servicebuilder.build();

				BrowserHistoryItemCollection collection = service.getBrowserHistory("menda91").execute();
				historylog=collection.getItems();
				
				for (int i = 0; i < historylog.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put(TAG_INDEX, historylog.get(i).getIndex());
					map.put(TAG_LINK, historylog.get(i).getTitle());
			
					// adding HashList to ArrayList
					webList.add(map);
					

				}

			} catch (Exception e) {
				Log.d("error", e.toString());
			}
			return null;
		}
	}
	
	public static class UserNamespace{
		
		private static final String BUILDURL="http://10.0.2.2:8888/_ah/api";
		
		static Userrelateddataendpoint.Builder servicebuilder = new Userrelateddataendpoint.Builder(
				AndroidHttp.newCompatibleTransport(),
				new GsonFactory(), null);
		
		public static Userrelateddataendpoint.Builder getServiceBuilder(){
			servicebuilder.setRootUrl(BUILDURL);
			return servicebuilder;
		}
		
	}

	
}
