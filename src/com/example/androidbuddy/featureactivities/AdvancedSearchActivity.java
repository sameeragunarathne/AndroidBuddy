package com.example.androidbuddy.featureactivities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import com.example.androidbuddy.R;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

public class AdvancedSearchActivity extends Activity implements
		OnItemSelectedListener {

	private ProgressDialog pDialog;

	Spinner spinner2;
	Spinner spinner3;
	AutoCompleteTextView contact;
	TextView subcategory;
	List<String> categories;
	List<String> callCategories;
	List<String> messegeCategories;
	List<String> contactCategories;
	List<String> timeCategories;
	List<String> contacts2;
	String[] sendData;
	Button from;
	Button to;
	Button search;
	TextView txt_to;
	TextView txt_from;
	private String spinner1Value;
	private String spinner2Value;
	private String spinner3Value;
	private boolean istxt_to = false, istxt_from = false, isSpinner2 = false,isDateFromFilled=false,isDateToFilled=false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_interface);

		Spinner spinner1 = (Spinner) findViewById(R.id.spinner);
		spinner2 = (Spinner) findViewById(R.id.spinner2);
		spinner3 = (Spinner) findViewById(R.id.spinner3);
		subcategory = (TextView) findViewById(R.id.sub_category);
		from = (Button) findViewById(R.id.btn_from);
		to = (Button) findViewById(R.id.btn_to);
		search = (Button) findViewById(R.id.btn_search);
		txt_to = (TextView) findViewById(R.id.txt_to);
		txt_from = (TextView) findViewById(R.id.txt_from);

		new ContactSearchAsyncTask().execute();

		spinner2.setVisibility(View.GONE);
		subcategory.setVisibility(View.GONE);
		to.setVisibility(View.GONE);
		from.setVisibility(View.GONE);
		txt_to.setVisibility(View.GONE);
		txt_from.setVisibility(View.GONE);

		spinner1.setOnItemSelectedListener(this);
		spinner2.setOnItemSelectedListener(this);
		spinner3.setOnItemSelectedListener(this);

		setSpinnerData();

		ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, categories);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner1.setAdapter(spinnerAdapter);

		ArrayAdapter<String> timespinnerAdapter = new ArrayAdapter<String>(
				this, android.R.layout.simple_spinner_item, timeCategories);
		spinnerAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spinner3.setAdapter(timespinnerAdapter);
	}

	//this async task searches contacts of the phone
	private class ContactSearchAsyncTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(AdvancedSearchActivity.this);
			pDialog.setMessage("Loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

		@Override
		protected Void doInBackground(Void... params) {

			contacts2 = getContacts();
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			pDialog.dismiss();
			contact = (AutoCompleteTextView) findViewById(R.id.autocomplete_contact);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getApplicationContext(),
					android.R.layout.simple_dropdown_item_1line, contacts2);
			contact.setAdapter(adapter);

		}

	}

	private void setSpinnerData() {
		categories = new ArrayList<String>();
		callCategories = new ArrayList<String>();
		messegeCategories = new ArrayList<String>();
		timeCategories = new ArrayList<String>();

		categories.add("Calls");
		categories.add("Messeges");

		callCategories.add("All");
		callCategories.add("OutGoing");
		callCategories.add("Missed");
		callCategories.add("Answered");

		messegeCategories.add("All");
		messegeCategories.add("Inbox");
		messegeCategories.add("OutBox");
		messegeCategories.add("Drafts");

		timeCategories.add("none");
		timeCategories.add("specific date");
		timeCategories.add("specific time period");
	}

	@Override
	public void onItemSelected(AdapterView<?> params, View arg1, int position,
			long arg3) {

		switch (params.getId()) {
		case R.id.spinner: {
			spinner1Value = params.getItemAtPosition(position).toString();
			Log.d("item", spinner1Value);
			if (spinner1Value.equals("Calls")) {
				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						callCategories);
				spinnerAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(spinnerAdapter);
				spinner2.setVisibility(View.VISIBLE);
				subcategory.setVisibility(View.VISIBLE);
				isSpinner2 = true;

			} else if (spinner1Value.equals("Messeges")) {
				ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(
						this, android.R.layout.simple_spinner_item,
						messegeCategories);
				spinnerAdapter
						.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				spinner2.setAdapter(spinnerAdapter);
				spinner2.setVisibility(View.VISIBLE);
				subcategory.setVisibility(View.VISIBLE);
				isSpinner2 = true;

			} else {
				spinner2.setVisibility(View.GONE);
				subcategory.setVisibility(View.GONE);
				isSpinner2 = false;
			}
		}
			break;

		case R.id.spinner2: {
			spinner2Value = params.getItemAtPosition(position).toString();

		}
			break;

		case R.id.spinner3: {
			spinner3Value = params.getItemAtPosition(position).toString();
			if (spinner3Value.equals("specific date")) {

				from.setVisibility(View.VISIBLE);
				from.setText("date");
				txt_from.setVisibility(View.VISIBLE);
				txt_from.setText("specific date(dd/MM/yy)");
				txt_to.setVisibility(View.GONE);
				to.setVisibility(View.GONE);
				istxt_from = true;
				istxt_to = false;
				isDateFromFilled=false;

			} else if (spinner3Value.equals("specific time period")) {
				to.setVisibility(View.VISIBLE);
				from.setVisibility(View.VISIBLE);
				txt_to.setVisibility(View.VISIBLE);
				txt_from.setVisibility(View.VISIBLE);
				txt_from.setText("starting date:(dd/MM/yy)");
				txt_to.setText("end date:(dd/MM/yy)");
				from.setText("from");
				istxt_from = true;
				istxt_to = true;
				isDateFromFilled=false;
				isDateToFilled=false;
			} else {
				to.setVisibility(View.GONE);
				from.setVisibility(View.GONE);
				txt_to.setVisibility(View.GONE);
				txt_from.setVisibility(View.GONE);
				txt_from.setText("starting date:(dd/MM/yy)");
				txt_to.setText("end date:(dd/MM/yy)");
				from.setText("from");
				istxt_from = false;
				istxt_to = false;
			}

		}
			break;

		default:
			break;
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> params) {

	}

	public void dateFrombuttonOnclick(View v) {

		returnDatePicker(v).show();

	}

	public void dateTobuttonOnclick(View v) {

		returnDatePicker(v).show();

	}

	private DatePickerDialog returnDatePicker(View v) {
		DatePickerDialog picker = null;
		try {
			Calendar cal = Calendar.getInstance(TimeZone.getDefault());
			switch (v.getId()) {
			case R.id.btn_to: {
				DatePickerDialog enddatePicker = new DatePickerDialog(this,
						R.style.AppTheme, enddatePickerListener,
						cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
				enddatePicker.setCancelable(false);
				enddatePicker.setTitle("Select the date");
				picker = enddatePicker;
			}
				break;
			case R.id.btn_from: {
				DatePickerDialog datePicker = new DatePickerDialog(this,
						R.style.AppTheme, startdatePickerListener,
						cal.get(Calendar.YEAR), cal.get(Calendar.MONTH),
						cal.get(Calendar.DAY_OF_MONTH));
				datePicker.setCancelable(false);
				datePicker.setTitle("Select the date");

				picker = datePicker;
			}
				break;

			default:
				break;
			}

		} catch (Exception e) {
			Log.d("messege", e.toString());
		}
		return picker;
	}

	private DatePickerDialog.OnDateSetListener enddatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			String year1 = String.valueOf(selectedYear);
			String month1 = String.valueOf(selectedMonth + 1);
			String day1 = String.valueOf(selectedDay);
			txt_to.setText(day1 + "/" + month1 + "/" + year1);
			isDateToFilled=true;
		}
	};

	private DatePickerDialog.OnDateSetListener startdatePickerListener = new DatePickerDialog.OnDateSetListener() {

		// when dialog box is closed, below method will be called.
		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			String year1 = String.valueOf(selectedYear);
			String month1 = String.valueOf(selectedMonth + 1);
			String day1 = String.valueOf(selectedDay);
			txt_from.setText(day1 + "/" + month1 + "/" + year1);
			isDateFromFilled=true;

		}
	};

	public void SearchButtonOnClick(View v) {

		sendData = new String[6];
		String txt1="", txt2="", txt3="", txt4="";
		if (istxt_from && isDateFromFilled)
			txt1 = txt_from.getText().toString();
		if (istxt_to && isDateToFilled)
			txt2=txt_to.getText().toString();
		if (isSpinner2)
			txt4 = spinner2Value;

		txt3 = contact.getText().toString();
		
		sendData[0]=spinner1Value;
		sendData[1]=txt4;
		sendData[2]=spinner3Value;
		sendData[3]=txt1;
		sendData[4]=txt2;
		sendData[5]=txt3;
		
		Bundle bundle=new Bundle();
		bundle.putStringArray("string_values",sendData);	
		Intent searchIntent=new Intent(this,SearchResultActivity.class);
		searchIntent.putExtras(bundle);
		startActivity(searchIntent);
		
		Log.d("inputs",spinner1Value+" : "+txt4 + " : " + spinner3Value + " : " + txt1+" : "+txt2+" : "+txt3);

	}

	// getting contacts list
	private List<String> getContacts() {

		List<String> phoneContactList = new ArrayList<String>();
		ContentResolver cr = getContentResolver();
		Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null,
				null, null, null);

		if (cur.getCount() > 0) {
			while (cur.moveToNext()) {
				String id = cur.getString(cur
						.getColumnIndex(ContactsContract.Contacts._ID));
				String name = cur
						.getString(cur
								.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
				if (Integer
						.parseInt(cur.getString(cur
								.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
					// This inner cursor is for contacts that have multiple
					// numbers.
					Cursor pCur = cr.query(
							ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
							null,
							ContactsContract.CommonDataKinds.Phone.CONTACT_ID
									+ " = ?", new String[] { id }, null);

					while (pCur.moveToNext()) {
						phoneContactList.add(name);
						Log.i("Contact List", name);
					}
					pCur.close();
				}
			}
		}
		phoneContactList.add(0, "None");
		return phoneContactList;
	}

}
