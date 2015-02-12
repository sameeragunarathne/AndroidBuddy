package com.example.androidbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.MessegeLogCollection;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;

public class OutboxActivity extends ListActivity {

	private ProgressDialog pDialog;

	private static final String TAG_FROM = "from";
	private static final String TAG_SUBJECT = "subject";
	private static final String TAG_DATE = "date";

	ArrayList<HashMap<String, String>> outboxList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outbox_list);
		outboxList = new ArrayList<HashMap<String, String>>();
		new LoadOutboxAsyncTask().execute();
		
	}

	private class LoadOutboxAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(OutboxActivity.this);
			pDialog.setMessage("Loading Outbox...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			List<MessegeLog> messegelog = null;
			try {

				Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = servicebuilder.build();

				MessegeLogCollection collection = service.listInboxMessegeLog(
						"sent", "menda91").execute();
				messegelog = (List<MessegeLog>) collection.getItems();

				for (int i = 0; i < messegelog.size(); i++) {

					HashMap<String, String> map = new HashMap<String, String>();				
					// adding each child node to HashMap key => value
					map.put(TAG_FROM, messegelog.get(i).getSender());
					map.put(TAG_SUBJECT, messegelog.get(i).getMessage());
					map.put(TAG_DATE, messegelog.get(i).getTime());
					// adding HashList to ArrayList
					outboxList.add(map);

				}

			} catch (Exception e) {
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
							OutboxActivity.this, outboxList,
							R.layout.outbox_list_item, new String[] { TAG_FROM,
									TAG_SUBJECT, TAG_DATE }, new int[] {
									R.id.to, R.id.send_subject, R.id.send_date });
					// updating listview
					setListAdapter(adapter);
				}
			});
			
		

		}

	}

}
