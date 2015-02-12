package com.example.androidbuddy;

import com.example.androidbuddy.services.BatteryNotifyService;

import android.app.ListActivity;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.ListView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//activity for application main menu
public class AppMenuActivity extends ListActivity{

	private String[] menulist={"featureactivities.AdvancedSearchActivity","LoginActivity","OptionMenuActivity","datastoreactivity.AddCallLogActivity","LogMenuListActivity"};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		registerReceiver(batteryInfoReceiver, new IntentFilter(
        	    Intent.ACTION_BATTERY_CHANGED));
		
		 // storing string resources into Array
        String[] app_options = getResources().getStringArray(R.array.app_options);
         
        // Binding resources Array to ListAdapter
        this.setListAdapter(new ArrayAdapter<String>(this, R.layout.list_item, R.id.label, app_options));
        
        ListView lv = getListView();
        
        // listening to single list item on click
        lv.setOnItemClickListener(new OnItemClickListener() {
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
          
//        unregisterReceiver(batteryInfoReceiver);
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.settings, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		  switch (item.getItemId()) {
		  
	        case R.id.menu_settings:
	            Intent i = new Intent(this, UserSettingsActivity.class);
	            startActivity(i);
	            break;
	 
	        }
	 
	        return true;

	}
	
	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
	    @Override
	    public void onReceive(Context context, Intent intent) {
			Log.d("received", "battery intent");
			Log.d("intent",intent.getAction());
			 
			        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
			        Intent batteryStatus = context.registerReceiver(null, ifilter);
			        Log.d("received", "battery low");
			        int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
			        int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
			        int percent = (level*100)/scale; 
			        
			        if (percent < 4 && percent > 2) {
			        	Intent newIntent=new Intent(context,BatteryNotifyService.class);
			        	context.startService(newIntent);
			        }
			    
	    }
	};
		
//	@Override
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//		super.onActivityResult(requestCode, resultCode, data);
//		
//	}

}
