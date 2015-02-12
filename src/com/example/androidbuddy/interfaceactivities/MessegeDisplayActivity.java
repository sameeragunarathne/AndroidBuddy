package com.example.androidbuddy.interfaceactivities;

import java.util.ArrayList;
import java.util.List;

import com.example.androidbuddy.InboxActivity;
import com.example.androidbuddy.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MessegeDisplayActivity extends Activity {

	private List<String> messege_details;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messege_item);
		messege_details = new ArrayList<String>();

		Intent intent = getIntent();
		messege_details = intent.getStringArrayListExtra("messege_results");
		TextView body = (TextView) findViewById(R.id.message_text);
		TextView number = (TextView) findViewById(R.id.message_number);

		Log.d("inputs", messege_details.get(1));

		body.setText(messege_details.get(1));
		number.setText(messege_details.get(0) + " (" + messege_details.get(3)
				+ ")");

	}

	@SuppressLint("InlinedApi")
	public void OnclickSendMessege(View v) {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				MessegeDisplayActivity.this);

		// Setting Dialog Title
		alertDialog.setTitle("SEND MESSEGE");

		// Setting Dialog Message
		alertDialog.setMessage("Enter Messege");
		final EditText input = new EditText(MessegeDisplayActivity.this);
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);
		input.setLayoutParams(lp);
		alertDialog.setView(input);

		alertDialog.setIcon(R.drawable.inbox_gray);

		alertDialog.setPositiveButton("Send",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface arg0, int arg1) {

						String phone_Num = messege_details.get(3);
						String messege = input.getText().toString();

						new SendAsyncClass().execute(phone_Num, messege);
					}
				});
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

					}
				});
		alertDialog.show();
	}

	private class SendAsyncClass extends AsyncTask<String, Void, String> {

		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(MessegeDisplayActivity.this);
			pDialog.setMessage("Sending...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {

			String Status;
			try {
				SmsManager sms = SmsManager.getDefault();
				Log.d("phone messege", params[1]);
				sms.sendTextMessage(params[0], null, params[1], null, null);
				Status = "Sms Sent";
			} catch (Exception e) {
				e.printStackTrace();
				Status = "Sms not Sent";
			}

			return Status;
		}

		@Override
		protected void onPostExecute(String result) {
			pDialog.dismiss();
			Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT)
					.show();
		}

	}

}
