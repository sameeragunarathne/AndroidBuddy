package com.example.androidbuddy;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;


public class AlarmReceiver extends BroadcastReceiver {

	 
	 @Override
	 public void onReceive(Context context, Intent intent) {
		 
		// Toast.makeText(context, getCallDetail(context), Toast.LENGTH_LONG).show();
		 Intent i = new Intent(context, TaskService.class);
		 context.startService(i);	 
		
	 }		

}
