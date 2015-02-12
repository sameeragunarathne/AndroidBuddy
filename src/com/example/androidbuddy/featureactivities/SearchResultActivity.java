package com.example.androidbuddy.featureactivities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Text;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.CallLogCollection;
import com.sep.androidbuddy.service.userrelateddataservice.model.MessegeLogCollection;

import com.example.androidbuddy.InboxActivity;
import com.example.androidbuddy.R;
import com.example.androidbuddy.interfaceactivities.MessegeDisplayActivity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;


public class SearchResultActivity extends ListActivity {

	private List<String> sentData;
	private ProgressDialog pDialog;
	
	ArrayList<HashMap<String, String>> callList;
	
	
	private static final String TAG_FROM = "from";
	private static final String TAG_DATE = "date";
	private static final String TAG_NUMBER = "number";
	List<String> params;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_list);
		
		Bundle bundle=this.getIntent().getExtras();
		params=new ArrayList<String>();
		String[] inputArray=bundle.getStringArray("string_values");

		if(inputArray[0].equals("Calls"))
		new LoadSearchCallResultsAsyncTask().execute(inputArray);
		
		ListView listview=getListView();
		listview.setOnItemClickListener(new OnItemClickListener() {
		       public void onItemClick(AdapterView<?> parent, View view,
		                int position, long id) {
		                 TextView from=(TextView)findViewById(R.id.call_person);		
		                 TextView date =(TextView)findViewById(R.id.missed_call_time);
		                 TextView number=(TextView)findViewById(R.id.call_num);
		            }
		});
		
	}


	private class LoadSearchCallResultsAsyncTask extends
			AsyncTask<String, Void, Void> {

		ArrayList<HashMap<String, String>> callList;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(SearchResultActivity.this);
			pDialog.setMessage("Loading Results...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(String... params) {
			callList = new ArrayList<HashMap<String, String>>();	
			List<CallLog> userCallList = new ArrayList<CallLog>();
//			Log.d("messege", "got herreeeeeeee");
			try {
				Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = servicebuilder.build();
				
				String keyword="";
				for (int i = 1; i < 3; i++) {
					keyword+=params[i];
					Log.d("keyword", keyword);
				}
				
				if(keyword.equals("Allnone")){
					
					CallLogCollection collection=service.listCallLogByUser("menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();	
					
					
				}else if(keyword.equals("Allspecific time period")){
					
					CallLogCollection collection=service.listCallLogByTimePeriod("menda91",params[3],params[4],params[1]).execute();
					userCallList=(List<CallLog>)collection.getItems();	
					
				}else if(keyword.equals("Allspecific date")){
					
					CallLogCollection collection=service.listCallLogBySecificDate(params[1],params[3],"menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();	
					
				}else if(keyword.equals("Missednone")){
					
					CallLogCollection collection=service.listMissedCallLog(params[1], "menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("Missedspecific time period")){
					
					CallLogCollection collection=service.listCallLogByTimePeriod("menda91",params[3],params[4],params[1]).execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("Missedspecific date")){
					
					CallLogCollection collection=service.listCallLogBySecificDate(params[1],params[3],"menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("OutGoingnone")){
					
					CallLogCollection collection=service.listMissedCallLog(params[1], "menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("OutGoingspecific time period")){
					
					CallLogCollection collection=service.listCallLogByTimePeriod("menda91",params[3],params[4],params[1]).execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("OutGoingspecific date")){
					
					CallLogCollection collection=service.listCallLogBySecificDate(params[1],params[3],"menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("Answerednone")){
					
					CallLogCollection collection=service.listMissedCallLog(params[1], "menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("Answeredspecific time period")){
					
					CallLogCollection collection=service.listCallLogByTimePeriod("menda91",params[3],params[4],params[1]).execute();
					userCallList=(List<CallLog>)collection.getItems();
					
				}else if(keyword.equals("Answeredspecific date")){
					
					CallLogCollection collection=service.listCallLogBySecificDate(params[1],params[3],"menda91").execute();
					userCallList=(List<CallLog>)collection.getItems();
				}
				
				try{
					for (int i = 0; i < userCallList.size(); i++) {
						HashMap<String, String> map = new HashMap<String, String>();
						map.put(TAG_FROM, userCallList.get(i).getName());
						map.put(TAG_DATE, userCallList.get(i).getDate());
						map.put(TAG_NUMBER, userCallList.get(i).getNumber());
						callList.add(map);
						
					}
				}catch(NullPointerException e){
					Log.d("error", "null pointer");
				}
			} catch (Exception e) {
				Log.d("messege", e.toString());
			}
			
			return null;
		}
		
		private class LoadSearchMessegeAsyncTask extends AsyncTask<String, Void, Void>{
			
			ArrayList<HashMap<String, String>> messegeList=new ArrayList<HashMap<String, String>>();
			List<MessegeLog> messegeLogList=new ArrayList<MessegeLog>();
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				pDialog = new ProgressDialog(SearchResultActivity.this);
				pDialog.setMessage("Loading Results...");
				pDialog.setIndeterminate(false);
				pDialog.setCancelable(false);
				pDialog.show();
			}
			
			@Override
			protected Void doInBackground(String... params) {
				
				try {
					Userrelateddataservice.Builder servicebuilder = new Userrelateddataservice.Builder(
							AndroidHttp.newCompatibleTransport(),
							new GsonFactory(), null);
					servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
					Userrelateddataservice service = servicebuilder.build();
					
					String keyword="";
					for (int i = 1; i < 3; i++) {
						keyword+=params[i];
						Log.d("keyword", keyword);
					}
					
					
					if(keyword.equals("Allnone")){
						
						MessegeLogCollection collection=service.listMessegeLogByUser("menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
						
					}else if(keyword.equals("Allspecific time period")){
						
						MessegeLogCollection collection=service.listMessegeLogByTimePeriod("menda91", params[3], params[4], params[1]).execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();		
						
					}else if(keyword.equals("Allspecific date")){
						
						MessegeLogCollection collection=service.listMessegeLogBySecificDate(params[1], params[3], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Inboxnone")){
						
						MessegeLogCollection collection=service.listMessegeBytype(params[1], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
											
					}else if(keyword.equals("Inboxspecific time period")){
						
						MessegeLogCollection collection=service.listMessegeLogByTimePeriod("menda91", params[3], params[4], params[1]).execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Inboxspecific date")){
						
						MessegeLogCollection collection=service.listMessegeLogBySecificDate(params[1], params[3], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Outboxnone")){
						
						MessegeLogCollection collection=service.listMessegeBytype(params[1], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Outboxspecific time period")){
						
						MessegeLogCollection collection=service.listMessegeLogByTimePeriod("menda91", params[3], params[4], params[1]).execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Outboxspecific date")){
						
						MessegeLogCollection collection=service.listMessegeLogBySecificDate(params[1], params[3], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Draftsnone")){
						
						MessegeLogCollection collection=service.listMessegeBytype(params[1], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Draftsspecific time period")){
						
						MessegeLogCollection collection=service.listMessegeLogByTimePeriod("menda91", params[3], params[4], params[1]).execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
						
					}else if(keyword.equals("Draftsspecific date")){
						
						MessegeLogCollection collection=service.listMessegeLogBySecificDate(params[1], params[3], "menda91").execute();
						messegeLogList=(List<MessegeLog>)collection.getItems();	
					}
					
					try{
						for (int i = 0; i < messegeLogList.size(); i++) {
							HashMap<String, String> map = new HashMap<String, String>();
							map.put(TAG_FROM, messegeLogList.get(i).getSender());
							SimpleDateFormat df=new SimpleDateFormat("dd-MM-yy HH:mm");
							map.put(TAG_DATE, messegeLogList.get(i).getTime());
							map.put(TAG_NUMBER, messegeLogList.get(i).getNumber());
							messegeList.add(map);
							
						}
					}catch(NullPointerException e){
						Log.d("error", "null pointer");
					}
					
								
				} catch (Exception e) {

				}
				return null;
			}
			
		}
		
		@Override
		protected void onPostExecute(Void result) {

			// dismiss the dialog after getting all products
			pDialog.dismiss();
			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					
					ListAdapter adapter = new SimpleAdapter(SearchResultActivity.this,
							callList, R.layout.missed_call_list_item, new String[] {
									TAG_FROM, TAG_DATE,TAG_NUMBER },
							new int[] { R.id.call_person, R.id.missed_call_time,R.id.call_num });
					// updating listview
					setListAdapter(adapter);
					
				}
			});
			
		}
		

	}
	

}
