package com.example.androidbuddy;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class MessegeLogTab extends TabActivity{

	private static final String INBOX_SPEC = "Inbox";
    private static final String OUTBOX_SPEC = "Outbox";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messege_log_tab);
		
		TabHost tabhost=getTabHost();
		
		//inbox tab
		TabSpec inboxspec=tabhost.newTabSpec(INBOX_SPEC);
		inboxspec.setIndicator(INBOX_SPEC,getResources().getDrawable(R.drawable.inbox_gray));
		Intent inboxIntent= new Intent(this,InboxActivity.class);	
		inboxspec.setContent(inboxIntent);
		
		// outbox tab
		TabSpec outboxspec=tabhost.newTabSpec(OUTBOX_SPEC);
		outboxspec.setIndicator(OUTBOX_SPEC,getResources().getDrawable(R.drawable.outbox_gray));
		Intent outboxIntent= new Intent(this,OutboxActivity.class);
		outboxspec.setContent(outboxIntent);
		
		
		//adding tabs to the tab host
		tabhost.addTab(inboxspec);
		tabhost.addTab(outboxspec);
				
	}

	
}
