package edu.virginia.dtc.DiAsService;

import java.util.Timer;
import java.util.TimerTask;

import edu.virginia.dtc.SysMan.State;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MDI_Activity extends Activity{
	
	public static final String TAG = "MDI_Activity";
	public static final boolean DEBUG = false;
	private static final int DIAS_SERVICE_COMMAND_START_SAFETY_CLICK = 26;
	public static final int MDI_ACTIVITY_STATUS_SUCCESS = 0;
	public static final int MDI_ACTIVITY_STATUS_TIMEOUT = -1;
	
	private Timer time;
	private int status;
	private int state_change_command = DIAS_SERVICE_COMMAND_START_SAFETY_CLICK;
	EditText mdi_injection;
	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		state_change_command = getIntent().getIntExtra("state_change_command", DIAS_SERVICE_COMMAND_START_SAFETY_CLICK);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mdiscreen);
		debug_message(TAG, "OnCreate");
		mdi_injection = (EditText)findViewById(R.id.insulin_injected);
//		initScreen();
	}
	
	@Override
	public void onStart()
	{
		super.onStart();
	}
	
	@Override
	public void onDestroy()
	{
		super.onDestroy();
		debug_message(TAG, "OnDestroy");
		finish();
	}
	
	@Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
    	super.onWindowFocusChanged(hasFocus);
    	
    	if(hasFocus)
    	{
    		debug_message(TAG, "M_HEIGHT: "+this.findViewById(R.id.alarmLayout).getHeight()+" M_WIDTH: "+this.findViewById(R.id.alarmLayout).getWidth());
    	}
    }

/*	
	private void initScreen()
	{
		time = new Timer();
		TimerTask timeout = new TimerTask()
		{
			public void run()
			{
				// If the screen times out then cancel - don't assume a bolus of zero
				Intent injection = new Intent("edu.virginia.dtc.intent.action.MDI_INJECTION");
				double insulin_injected = 0.0;
				injection.putExtra("insulin_injected", insulin_injected);
				injection.putExtra("state_change_command", state_change_command);
				injection.putExtra("status", MDI_ACTIVITY_STATUS_TIMEOUT);
				sendBroadcast(injection);
				finish();
			}
		};
		time.schedule(timeout, 3*60*1000);			// 3 minute timeout
	}
*/
	
	/************************************************************************************
	* Action Listeners
	************************************************************************************/
	
	public void okClick(View view) 
	{
		Intent injection = new Intent("edu.virginia.dtc.intent.action.MDI_INJECTION");
		Double insulin_injected;
		if (mdi_injection.getText().toString().equalsIgnoreCase("")) {
			insulin_injected = 0.0;
		}
		else {
			insulin_injected = Double.parseDouble(mdi_injection.getText().toString());
		}
		injection.putExtra("insulin_injected", insulin_injected);
		injection.putExtra("state_change_command", state_change_command);
		injection.putExtra("status", MDI_ACTIVITY_STATUS_SUCCESS);
		sendBroadcast(injection);
//		time.cancel();	
		finish();
    }
	
//	public void cancelClick(View view)
//	{
//		time.cancel();
//		finish();
//	}
	
	/************************************************************************************
	* Auxillary Functions
	************************************************************************************/
	
	public long getCurrentTimeSeconds() {
		long currentTimeSeconds = (long)(System.currentTimeMillis()/1000);			// Seconds since 1/1/1970 in UTC
		return currentTimeSeconds;
	}
	
	/************************************************************************************
	* Log Messaging Functions
	************************************************************************************/
	
	private void debug_message(String tag, String message) {
		if (DEBUG) {
			Log.i(tag, message);
		}
	}
	
	public void log_IO(String tag, String message) {
		Log.i(tag, message);
		
		/*
		Intent i = new Intent("edu.virginia.dtc.intent.action.LOG_ACTION");
        i.putExtra("Service", tag);
        i.putExtra("Status", message);
        i.putExtra("time", (long)getCurrentTimeSeconds());
        sendBroadcast(i);
        */
	}
}
