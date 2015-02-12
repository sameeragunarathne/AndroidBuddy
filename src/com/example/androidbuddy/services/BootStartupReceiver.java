package com.example.androidbuddy.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootStartupReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent service = new Intent(context, OnBootRunService.class);
        context.startService(service);
	}

}
