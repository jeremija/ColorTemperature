package com.steinerize.android.colortemperature;

import com.steinerize.android.colortemperature.settings.Defaults;
import com.steinerize.android.colortemperature.settings.Keys;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.IBinder;
import android.util.Log;
import android.view.WindowManager;

public class FilterService extends Service {
	
	private static final int ONGOING_NOTIFICATION = 1;
	
	private static final String TAG = "FilterService"; 
	private static boolean running = false;
	
	private final Color color = new Color();
	private int opacity = 0;
	
	private int orientation;
	
	public static boolean isRunning() {
		return running;
	}
	
	private FilterView view;

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate() {
		super.onCreate();
		view = new FilterView(this);
		
		PendingIntent pi = PendingIntent.getActivity(this, 0, 
				new Intent(this, SettingsActivity.class), 0);
		
		Notification notification = new Notification.Builder(this)
			.setSmallIcon(R.drawable.ic_launcher)
			.setContentTitle("Color Temperature")
			.setContentText("Filter is active. Touch to configure...")
			.setContentIntent(pi)
			.getNotification(); 
		
		startForeground(ONGOING_NOTIFICATION, notification);
		
		orientation = getResources().getConfiguration().orientation;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation != orientation) {
			orientation = newConfig.orientation;
			WindowManager wm = getWindowManager();
			if (view.isAttached()) view.detach(wm).attach(wm);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		view.detach(getWindowManager());
		running = false;
		Log.i(TAG, "#onDestroy()");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		
		opacity = intent.getIntExtra(Keys.OPACITY, Defaults.OPACITY);
		int colorTemp = intent.getIntExtra(Keys.COLOR_TEMP, Defaults.COLOR_TEMP);
		int darkness = intent.getIntExtra(Keys.DARKNESS, Defaults.DARKNESS);
		
		color.setDarkness(darkness).setColorTemp(colorTemp);			

		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		
		Log.i(TAG, "#onStartCommand() values: " + opacity + "," + colorTemp + "," + darkness);
		
		view.setColor(opacity, r, g, b).attach(getWindowManager());
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	private WindowManager getWindowManager() {
		return (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	@Override
	public IBinder onBind(Intent intent) {
		Log.d(TAG, "FilterService#onBind(intent)");
		return null;
	}

}
