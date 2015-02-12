package com.example.androidbuddy.datastoreactivity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.example.androidbuddy.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.Contact;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.ContactCollection;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.User;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

@SuppressLint("UseValueOf")
public class AddCallLogActivity extends Activity{

	EditText idNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_calllog);
		
		idNum=(EditText)findViewById(R.id.txt_id);
		
	}
	
	public void addClick(View v){
		
		String Id =idNum.getText().toString();
		Log.d("message", "Got here");
		String[] params={Id,"das"};
		AddCallLogAsyncTask task=new AddCallLogAsyncTask();
		task.execute(params[0],params[1]);		
	}

	 private class AddCallLogAsyncTask extends AsyncTask<String, Void, CallLog>{

         @Override
		protected CallLog doInBackground(String... params) {
			
			CallLog calllog=null;
			User user1=null;
			Log.d("message", "Got here");
			
			try{
				
				Userrelateddataendpoint.Builder builder=new Userrelateddataendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				Userrelateddataservice.Builder builder2=new Userrelateddataservice.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		//		Userendpoint.Builder userbuilder=new Userendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				builder.setRootUrl("http://10.0.2.2:8888/_ah/api");
		//		userbuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				
				Userrelateddataendpoint service=builder.build();
				Userrelateddataservice service2=builder2.build();
				
				user1=service.getUser("0000001").execute();
				
				List<Contact> list=new ArrayList<Contact>();
				ContactCollection collection=service.getUserContactList("0000001").execute();
				list=(List<Contact>)collection.getItems();
				
				Contact contact=new Contact();
				contact.setNumber(params[0]);
				contact.setEmail("menda@gmail.com");
				contact.setName("menda123");
//				service.insertContact(contact).execute();
		//		user1.getContactList().add(contact);
		//		user1.setName("new name");
	//			service.addContact("0000001", contact).execute();
				Log.d("message", "success");
				
				
			}catch(Exception e){
				
				Log.d("Could not Add call log", e.getMessage(), e);
			}
									
			return calllog;
		}

	
		
		
	 }
	
}
