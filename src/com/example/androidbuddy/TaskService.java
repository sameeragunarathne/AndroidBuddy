package com.example.androidbuddy;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Browser;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.Telephony;
import android.util.Log;
import android.widget.Toast;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import com.sep.androidbuddy.entity.userrelateddataendpoint.Userrelateddataendpoint;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.BrowserHistoryItem;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.MessegeLog;
import com.sep.androidbuddy.service.userrelateddataservice.Userrelateddataservice;
import com.sep.androidbuddy.entity.userrelateddataendpoint.model.Contact;

/*********** This Service class handles background service for updating logs **********/
public class TaskService extends IntentService {

	File myInternalFile;

	public final static String APP_PREFERENCES = "AppPrefs";
	public final static String LASTCALLID = "LastcallId";
	public final static String LASTMESSEGEID = "LastMessegeId";
	public final static String LASTCONTACTID = "LastContactId";

	SharedPreferences preferences;
	SharedPreferences.Editor editor;

	public TaskService() {

		super("TaskService");
	}

	@Override
	public void onCreate() {
		super.onCreate();

		getLastLogDetailsAsyncTask task = new getLastLogDetailsAsyncTask();
		task.execute("menda91");

	}

	@Override
	protected void onHandleIntent(Intent intent) {

	}

	// access call log details
	private void getCallDetails(Context context,
			Userrelateddataendpoint entityservice, String userId)
			throws ParseException, IOException {

		preferences = getSharedPreferences(APP_PREFERENCES,
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		Cursor cursor = context.getContentResolver().query(
				CallLog.Calls.CONTENT_URI, null, null, null,
				CallLog.Calls.DATE + " DESC");
		int number = cursor.getColumnIndex(CallLog.Calls.NUMBER);
		int type = cursor.getColumnIndex(CallLog.Calls.TYPE);
		int date = cursor.getColumnIndex(CallLog.Calls.DATE);
		int duration = cursor.getColumnIndex(CallLog.Calls.DURATION);
		int id = cursor.getColumnIndex(CallLog.Calls._ID);

		String flag = preferences.getString(LASTCALLID, "");
		String logId = "";

		while (cursor.moveToNext()) {
			if (cursor.getPosition() == 0) {
				logId = cursor.getString(id);
			}

			// if (flag.equals(cursor.getString(id)))
			// break;

			com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog newcall = new com.sep.androidbuddy.entity.userrelateddataendpoint.model.CallLog();

			Long seconds = cursor.getLong(date);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
			String dateString = formatter.format(new Date(seconds));
			Date datetime = new SimpleDateFormat("dd-MM-yy HH:mm")
					.parse(dateString);

			String phNumber = cursor.getString(number).trim();
			String callname = getContactName(context, phNumber);
			String callType = cursor.getString(type);
			String callDuration = cursor.getString(duration);
			String dir = null;

			int dircode = Integer.parseInt(callType);
			switch (dircode) {

			case CallLog.Calls.OUTGOING_TYPE:
				dir = "OUTGOING";
				break;

			case CallLog.Calls.INCOMING_TYPE:
				dir = "INCOMING";
				break;

			case CallLog.Calls.MISSED_TYPE:
				dir = "MISSED";
				break;
			}
			try {
				newcall.setNumber(phNumber);
				newcall.setName(callname);
				newcall.setType(dir);
				newcall.setDuration(callDuration);
				newcall.setDate(dateString);
				entityservice.addCallLog(userId, newcall).execute();
			} catch (Exception e) {
				Log.d("messege2", e.toString());
			}

		}
		editor.putString(LASTCALLID, logId);
		editor.commit();
		cursor.close();

	}

	// method for getting messege details
	@SuppressLint("InlinedApi")
	private void getMessegeDetails(Context context,
			Userrelateddataendpoint entityservice, String userId)
			throws IOException {

		preferences = getSharedPreferences(APP_PREFERENCES,
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		Uri uriSMSURI = Uri.parse("content://sms/");
		Cursor cursor = context.getContentResolver().query(uriSMSURI, null,
				null, null, null);
		int number = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
		int type = cursor.getColumnIndex(Telephony.Sms.TYPE);
		int body = cursor.getColumnIndex(Telephony.Sms.BODY);
		int id = cursor.getColumnIndex(Telephony.Sms._ID);
		int date = cursor.getColumnIndex(Telephony.Sms.DATE);
		String sms = "";
		String flag = preferences.getString(LASTMESSEGEID, "");
		String logId = "";

		while (cursor.moveToNext()) {
			if (cursor.getPosition() == 0) {
				logId = cursor.getString(id);
			}
			// if (flag.equals(cursor.getString(id)))
			// break;
			MessegeLog newMessege = new MessegeLog();

			Long seconds = cursor.getLong(date);
			SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yy HH:mm");
			String dateString = formatter.format(new Date(seconds));
			String phNumber = cursor.getString(number);
			String smstype = cursor.getString(type);
			String smsbody = cursor.getString(body);
			String person = getContactName(context, phNumber);
			sms = "From :" + phNumber + " : " + " : " + person + " : "
					+ smsbody + " :" + smstype + " : " + dateString;

			int dircode = Integer.parseInt(smstype);
			String dir = null;

			switch (dircode) {

			case 1:
				dir = "inbox";
				break;
			case 2:
				dir = "sent";
				break;
			case 3:
				dir = "draft";
				break;
			case 4:
				dir = "outbox";
				break;
			}
			newMessege.setNumber(phNumber);
			newMessege.setSender(person);
			newMessege.setMessage(smsbody);
			newMessege.setType(dir);
			newMessege.setTime(dateString);
			Log.d("text:", sms);
			entityservice.addMessegeLog(userId, newMessege).execute();

		}
		editor.putString(LASTMESSEGEID, logId);
		editor.commit();

	}

	// method for getting contact names
	public static String getContactName(Context context, String phoneNumber) {
		ContentResolver cr = context.getContentResolver();
		Uri uri = Uri.withAppendedPath(PhoneLookup.CONTENT_FILTER_URI,
				Uri.encode(phoneNumber));
		Cursor cursor = cr.query(uri,
				new String[] { PhoneLookup.DISPLAY_NAME }, null, null, null);
		if (cursor == null) {
			return null;
		}
		String contactName = null;
		if (cursor.moveToFirst()) {
			contactName = cursor.getString(cursor
					.getColumnIndex(PhoneLookup.DISPLAY_NAME));
		}
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
		return contactName;
	}

	// getting contact list for userId
	private void getContactlist(Context context,
			Userrelateddataendpoint entityservice, String userId)
			throws IOException {

		preferences = getSharedPreferences(APP_PREFERENCES,
				Context.MODE_PRIVATE);
		editor = preferences.edit();

		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		while (cur.moveToNext()) {

			String id = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts._ID));
			String name = cur.getString(cur
					.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
			String number = cur
					.getString(cur
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

			if (Integer
					.parseInt(cur.getString(cur
							.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
				// This inner cursor is for contacts that have multiple
				// numbers.
				Cursor pCur = cr.query(
						ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
						null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID
								+ " = ?", new String[] { id }, null);

				while (pCur.moveToNext()) {
					Contact contact = new Contact();
					Log.i("Contact List", name + " : " + number);
					contact.setName(name);
					contact.setNumber(number);
					// entityservice.addContact(userId, contact).execute();
				}
				pCur.close();
			}
		}

	}

	private void storeWebhistory(Context context,
			Userrelateddataendpoint entityservice, String userId)
			throws IOException {

		Cursor mCur = getContentResolver().query(Browser.BOOKMARKS_URI,
				Browser.HISTORY_PROJECTION, null, null, null);
		if (mCur.moveToFirst()) {
			while (mCur.isAfterLast() == false) {
				String title = mCur
						.getString(Browser.HISTORY_PROJECTION_TITLE_INDEX);
				String index = mCur
						.getString(Browser.HISTORY_PROJECTION_URL_INDEX);
				BrowserHistoryItem historyItem = new BrowserHistoryItem();
				historyItem.setIndex(index);
				historyItem.setTitle(title);

				entityservice.addHistory(userId, historyItem).execute();
				mCur.moveToNext();
			}
		}
	}

	private class getLastLogDetailsAsyncTask extends
			AsyncTask<String, Void, Date> {

		@SuppressWarnings("deprecation")
		@SuppressLint("SimpleDateFormat")
		@Override
		protected Date doInBackground(String... params) {
			try {
				// Userrelateddataservice.Builder servicebuilder = new
				// Userrelateddataservice.Builder(
				// AndroidHttp.newCompatibleTransport(),
				// new GsonFactory(), null);
				// servicebuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				// Userrelateddataservice service = servicebuilder.build();
				//
				// Userrelateddataendpoint.Builder endpointbuilder = new
				// Userrelateddataendpoint.Builder(
				// AndroidHttp.newCompatibleTransport(),
				// new GsonFactory(), null);
				// endpointbuilder.setRootUrl("http://10.0.2.2:8888/_ah/api");
				// Userrelateddataendpoint entityservice =
				// endpointbuilder.build();

				// getCallDetails(getApplicationContext(), entityservice,
				// params[0]);
				// // getMessegeDetails(getApplicationContext(), entityservice,
				// params[0]);
				// storeWebhistory(getApplicationContext(), entityservice,
				// params[0]);
	//			getContactlist(getApplicationContext(), null, params[0]);
				getcontacts();
			} catch (Exception e) {
				Log.d("errors", e.toString());
			}

			return null;
		}
	}

	private void getcontacts() {
		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		StringBuffer output = new StringBuffer();

		ContentResolver contentResolver = getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {

				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));
				
				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));

				if (hasPhoneNumber > 0) {
					output.append("\n First Name:" + name);
					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						output.append("\n Phone number:" + phoneNumber);					
					}
					phoneCursor.close();
					
					// Query and loop for every email of the contact
					Cursor emailCursor = contentResolver.query(
							EmailCONTENT_URI, null, EmailCONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (emailCursor.moveToNext()) {

						email = emailCursor.getString(emailCursor
								.getColumnIndex(DATA));
						
						output.append("\nEmail:" + email);
					}
					emailCursor.close();
				}
				output.append("\n");
			}		
			Log.d("contact", output.toString());
		}
		
		

	}

}
