package com.example.androidbuddy;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.User;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class RegisterActivity extends Activity{

	public final static String APP_PREFERENCES="AppPrefs"; 
	public final static String REGISTERED="notRegistered"; 
	
	SharedPreferences preferences;
	EditText userName;
	EditText email;
	EditText password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		userName=(EditText)findViewById(R.id.reg_fullname);
		email=(EditText)findViewById(R.id.reg_email);
		password=(EditText)findViewById(R.id.reg_password);
			
	}
	
	public void RegisterOnClick(View v){
	
		preferences=getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
		if(preferences.contains(REGISTERED)){
			Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
			startActivity(intent);
		}else{

			String usrname=userName.getText().toString();
			String mailaddr=email.getText().toString();
			String pwd=password.getText().toString();
			new RegisterAsyncTask().execute(usrname,mailaddr,pwd);
		}	
	}
	
	private class RegisterAsyncTask extends AsyncTask<String, Void, Boolean>{

		private ProgressDialog pDialog;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(RegisterActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}
		
		@Override
		protected Boolean doInBackground(String... params) {
			Boolean status=false;
			
			try{
				Userrelateddataendpoint.Builder builder=new Userrelateddataendpoint.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
				builder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataendpoint userservice=builder.build();
				
				User appUser=new User();					
				appUser.setUserId(params[0]);
				appUser.setPassword(params[2]);
				userservice.insertUser(appUser).execute();
				status=true;
								
			}catch(Exception e){
				status=false;
				Log.d("error", e.toString());
			}
			
			return status;
		}

		@Override
		protected void onPostExecute(Boolean result) {
		
			pDialog.dismiss();
			if(result){
				
				Editor editor=preferences.edit();
				editor.putString(REGISTERED, "registered");
				editor.commit();
							
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						RegisterActivity.this);
				// Setting Dialog Title
				alertDialog.setTitle("INFO");
				alertDialog.setIcon(android.R.drawable.ic_dialog_info);
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {
								
								Intent intent=new Intent(RegisterActivity.this,AppMenuActivity.class);
								startActivity(intent);
							}
						});
				alertDialog.show();
				
			}
			else{
				AlertDialog.Builder alertDialog = new AlertDialog.Builder(
						RegisterActivity.this);

				// Setting Dialog Title
				alertDialog.setTitle("REGISTRATION ERROR");
				alertDialog.setIcon(android.R.drawable.ic_dialog_info);
				alertDialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface arg0, int arg1) {

							}
						});
				alertDialog.show();
			}
			
		}
		
		
		
	}

	
}
