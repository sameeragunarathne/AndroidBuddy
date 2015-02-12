package com.example.androidbuddy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.service.userrelateddataservice.model.BooleanCollection;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.LastLogDetail;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.model.LastLogDetailCollection;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends Activity {

	EditText username;
	EditText password;
	TextView link;

	public final static String APP_PREFERENCES = "AppPrefs";
	public final static String REGISTERED = "notRegistered";

	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);

		username = (EditText) findViewById(R.id.txtUserName);
		password = (EditText) findViewById(R.id.txtPassword);
		link = (TextView) findViewById(R.id.link_to_register);

		link.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				preferences = getSharedPreferences(APP_PREFERENCES,
						Context.MODE_PRIVATE);
				if (!preferences.contains(REGISTERED)) {
					Intent nwintent = new Intent(LoginActivity.this,
							RegisterActivity.class);
					startActivity(nwintent);
				}
				else{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(
							LoginActivity.this);

					// Setting Dialog Title
					alertDialog.setTitle("INFO");
					alertDialog.setMessage("You Have Already Registered.Please Login.");
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
		});

	}

	public void LoginBtnClick(View v) {
		preferences = getSharedPreferences(APP_PREFERENCES,
				Context.MODE_PRIVATE);

		if (preferences.contains(REGISTERED))
			new LoginAsyncTask(this).execute(username.getText().toString(),
					password.getText().toString());
		else {
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					LoginActivity.this);

			// Setting Dialog Title
			alertDialog.setTitle("INFO");
			alertDialog.setMessage("Please Register");
			alertDialog.setIcon(android.R.drawable.ic_dialog_info);
			alertDialog.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface arg0, int arg1) {

						}
					});
			alertDialog.show();
		}
		// getCallDetails(this);
	}

	private class LoginAsyncTask extends AsyncTask<String, Void, Boolean> {

		Context context;
		private ProgressDialog pd;

		public LoginAsyncTask(Context context) {
			this.context = context;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pd = new ProgressDialog(context);
			pd.setMessage("Connecting..");
			pd.show();
		}

		@Override
		protected Boolean doInBackground(String... params) {

			List<Boolean> list = null;
			Boolean authMessege = false;
			List<LastLogDetail> list2 = null;

			try {
				Userrelateddataservice.Builder builder = new Userrelateddataservice.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				builder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataservice service = builder.build();

				BooleanCollection collection = service.login(params[0],
						params[1]).execute();
				list = (List<Boolean>) collection.getItems();

				authMessege = list.get(0);

				Userrelateddataendpoint.Builder endpointbuilder = new Userrelateddataendpoint.Builder(
						AndroidHttp.newCompatibleTransport(),
						new GsonFactory(), null);
				endpointbuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				Userrelateddataendpoint entityservice = endpointbuilder.build();

			} catch (Exception e) {
				Log.d("messege", e.toString());
			}

			return authMessege;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			pd.dismiss();
			if (result.booleanValue()) {
				Intent intent = new Intent(LoginActivity.this,
						AppMenuActivity.class);
				startActivity(intent);
				Log.d("messege", "success");

			} else {

				new AlertDialog.Builder(context)
						.setTitle("Login Fail")
						.setMessage("Incorrect username or password")
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int which) {

									}
								}).setIcon(android.R.drawable.ic_dialog_alert)
						.show();

				username.setText("");
				password.setText("");
			}
		}

	}


}
