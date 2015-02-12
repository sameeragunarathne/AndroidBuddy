package com.example.androidbuddy;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.androidbuddy.interfaceactivities.MessegeDisplayActivity;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.MessegeLogCollection;

public class InboxActivity extends ListActivity {

	private ProgressDialog pDialog;

	private static final String TAG_FROM = "from";
	private static final String TAG_SUBJECT = "subject";
	private static final String TAG_DATE = "date";
	private static final String TAG_NUMBER = "number";

	ArrayList<HashMap<String, String>> inboxList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.inbox_list);

		inboxList = new ArrayList<HashMap<String, String>>();	
		new LoadInboxAsyncTask().execute();
		ListView lv = getListView();
		
        lv.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                int position, long id) {
                 
            	TextView from=(TextView)view.findViewById(R.id.from);
            	TextView subject=(TextView)view.findViewById(R.id.subject);
            	TextView date=(TextView)view.findViewById(R.id.date);
            	TextView number=(TextView)view.findViewById(R.id.messege_number);
                List<String> params=new ArrayList<String>();
                
                params.add(from.getText().toString());
                params.add(subject.getText().toString());
                params.add(date.getText().toString());
                params.add(number.getText().toString());
                
        		Intent messegeIntent=new Intent(getApplicationContext(),MessegeDisplayActivity.class);
        		messegeIntent.putStringArrayListExtra("messege_results", (ArrayList<String>) params);
        		startActivity(messegeIntent);
                
            }
          });
        
       

	}

	private class LoadInboxAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(InboxActivity.this);
			pDialog.setMessage("Loading Inbox...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {

			List<MessegeLog> messegelog = null;
			try {

				Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = servicebuilder.build();

				MessegeLogCollection collection = service.listInboxMessegeLog(
						"inbox", "menda91").execute();
				messegelog = (List<MessegeLog>) collection.getItems();

				

				for (int i = 0; i < messegelog.size(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();
					// adding each child node to HashMap key => value
					map.put(TAG_FROM, messegelog.get(i).getSender());
					map.put(TAG_SUBJECT, messegelog.get(i).getMessage());
					map.put(TAG_DATE, messegelog.get(i).getTime());
					map.put(TAG_NUMBER, messegelog.get(i).getNumber());
					// adding HashList to ArrayList
					inboxList.add(map);
					
					Log.d("messege", messegelog.get(i).getSender());

				}

			} catch (Exception e) {
				Log.d("error", e.toString());
			}
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {

			// dismiss the dialog after getting all results
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					
					ListAdapter adapter = new SimpleAdapter(InboxActivity.this,
							inboxList, R.layout.inbox_list_item, new String[] {
									TAG_FROM, TAG_SUBJECT, TAG_DATE,TAG_NUMBER },
							new int[] { R.id.from, R.id.subject, R.id.date,R.id.messege_number });
					// updating listview
					setListAdapter(adapter);
				}
			});
			
			

		}

	}

}
