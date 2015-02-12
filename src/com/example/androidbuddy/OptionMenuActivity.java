package com.example.androidbuddy;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

public class OptionMenuActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.check_option_list);
		
	}
	
	public void onCheckboxClicked(View v){
				
		
		boolean checked = ((CheckBox) v).isChecked();
	    
	    switch(v.getId()) {
	        case R.id.calllog:
	            if (checked)
	            	setAlarm(true);
	            else
	            	setAlarm(false);
	            break;
	            
	        case R.id.messegelog:
	        	if (checked)
	            	setAlarm(true);    
	            break;
	        
	    }
		
	}
	
	public void setAlarm(boolean isset){
		
		Intent intentAlarm= new Intent(this, AlarmReceiver.class);
		AlarmManager alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);
	           
		final PendingIntent pIntent = PendingIntent.getBroadcast(this,1234567, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);	
		if(isset)
		alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 10000000, pIntent);
		else if(!isset)
		alarmManager.cancel(pIntent);	
		
		
	}

}
