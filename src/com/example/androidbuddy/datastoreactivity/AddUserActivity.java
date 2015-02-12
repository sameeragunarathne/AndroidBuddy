package com.example.androidbuddy.datastoreactivity;

import com.example.androidbuddy.R;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.User;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;


public class AddUserActivity extends Activity{

	EditText userName;
	EditText password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_user);
		
		userName=(EditText)findViewById(R.id.txtUserName);
		password=(EditText)findViewById(R.id.txtPwd);
		
	}

	public void addBtnClick(View v){
		
		String username=userName.getText().toString();
		String passwrd=password.getText().toString();
		String[] params={username,passwrd};
		
		new AddUserAsyncTask().execute(params);
		
		
		
	}
	
	 private class AddUserAsyncTask extends AsyncTask<String, Void, User>{

		@Override
		protected User doInBackground(String... params) {
			
			User user=null;
			
			try{
				Userrelateddataendpoint.Builder builder=new Userrelateddataendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				builder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				
				Userrelateddataendpoint service=builder.build();
				User appuser=new User();
				appuser.setPassword(params[1]);
				appuser.setName(params[0]);
			
				
				
			}catch(Exception e){
				
				
			}
			
			return null;
		}

	 }
	
}
