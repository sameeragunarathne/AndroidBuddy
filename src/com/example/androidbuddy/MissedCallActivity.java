package com.example.androidbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.CallLogCollection;
import com.sep.androidbuddy.service.userrelateddataservice.model.MessegeLogCollection;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class MissedCallActivity extends ListActivity{

	private ProgressDialog pDialog;

	private static final String TAG_FROM = "from";
	private static final String TAG_TYPE = "subject";
	private static final String TAG_DATE = "date";
	
	ArrayList<HashMap<String, String>> callList;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		callList = new ArrayList<HashMap<String, String>>();	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.missed_call_list);
		new LoadCallAsyncTask().execute();
	}
	
	private class LoadCallAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MissedCallActivity.this);
			pDialog.setMessage("Loading Calls...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		
		@Override
		protected Void doInBackground(Void... params) {
			List<CallLog> calllog = null;
			
			try {

				Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = servicebuilder.build();
				
				CallLogCollection collection=service.listMissedCallLog("missed", "menda91").execute();
				calllog=(List<CallLog>)collection.getItems();
				
				
				
				for (int i = 0; i < calllog.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					Log.d("input", calllog.get(i).getNumber());
					if(calllog.get(i).getName()!=null)
					map.put(TAG_FROM, calllog.get(i).getName());
					else
					map.put(TAG_FROM, calllog.get(i).getNumber());	
					
					map.put(TAG_DATE, calllog.get(i).getDate().toString());
					// adding HashList to ArrayList
					callList.add(map);

				}
				
				
			}catch(Exception e){
				Log.d("error", e.toString());
			}
			
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {

			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					ListAdapter adapter = new SimpleAdapter(
							MissedCallActivity.this, callList,
							R.layout.missed_call_list_item, new String[] { TAG_FROM,TAG_DATE }, new int[] {
									R.id.call_person, R.id.missed_call_time });
					// updating listview
					setListAdapter(adapter);
				}
			});

		}
		
		
		
	}
	
}
