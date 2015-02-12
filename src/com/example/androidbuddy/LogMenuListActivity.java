package com.example.androidbuddy;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LogMenuListActivity extends ListActivity{

	private String[] menulist={"CallLogTab","MessegeLogTab","WebHistoryActivity"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		String[] log_options=getResources().getStringArray(R.array.log_options);
		
		this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item,R.id.label,log_options));
		
		ListView listview = getListView();
		listview.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view,
		              int position, long id) {
		               
		              // selected item 
		              String list = ((TextView)view).getText().toString();
		              try {
						Class listClass=Class.forName("com.example.androidbuddy."+menulist[position]);
						
						Intent intent = new Intent(getApplicationContext(), listClass);
			            intent.putExtra("listItem", list);
			            startActivity(intent);
						
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
						Toast.makeText(getApplicationContext(), ""+e, Toast.LENGTH_LONG).show();
					}
		               		            
		          }
		});
	}

	
}
