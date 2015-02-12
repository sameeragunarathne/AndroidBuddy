package com.example.androidbuddy;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class CallLogTab extends TabActivity{

	private static final String MISSEDCALL_SPEC = "Missed";
    private static final String DIALLEDCALL_SPEC = "Dialled";
    private static final String RECIEVEDCALL_SPEC = "Incoming";
    
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.call_log_tab);
		
		TabHost tabhost=getTabHost();
		
		//missed call  tab
		TabSpec missedspec=tabhost.newTabSpec(MISSEDCALL_SPEC);
		missedspec.setIndicator(MISSEDCALL_SPEC,getResources().getDrawable(R.drawable.inbox_gray));
		Intent missedIntent= new Intent(this,MissedCallActivity.class);	
		missedspec.setContent(missedIntent);
		
		//dialled call tab
		TabSpec dialledspec=tabhost.newTabSpec(DIALLEDCALL_SPEC);
		dialledspec.setIndicator(DIALLEDCALL_SPEC,getResources().getDrawable(R.drawable.inbox_gray));
		Intent dialledIntent= new Intent(this,InboxActivity.class);	
		dialledspec.setContent(dialledIntent);
		
		//received tab
		TabSpec recievedspec=tabhost.newTabSpec(RECIEVEDCALL_SPEC);
		recievedspec.setIndicator(RECIEVEDCALL_SPEC,getResources().getDrawable(R.drawable.outbox_gray));
		Intent recievedIntent= new Intent(this,OutboxActivity.class);
		recievedspec.setContent(recievedIntent);
		
		//adding tabs to the tab host
		tabhost.addTab(missedspec);
		tabhost.addTab(dialledspec);
		tabhost.addTab(recievedspec);
		
		
	}

	
}
