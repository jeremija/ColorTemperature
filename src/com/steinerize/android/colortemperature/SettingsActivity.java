package com.steinerize.android.colortemperature;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.steinerize.android.colortemperature.callbacks.ServiceStateChangeListener;
import com.steinerize.android.colortemperature.settings.MainSettingsFragment;

public class SettingsActivity extends Activity implements ServiceStateChangeListener {
	
//	private static final String TAG = "Settings";
	
	private MainSettingsFragment mainSettings;
	private MenuItem startStopMenuItem;
	
	private static final String KEY_MAIN_SETTINGS_FRAGMENT = "KEY_MAIN_SETTINGS_FRAGMENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        if (savedInstanceState != null) {
        	mainSettings = (MainSettingsFragment)
        		getFragmentManager().getFragment(savedInstanceState, KEY_MAIN_SETTINGS_FRAGMENT);
        	return;
        }
        
        mainSettings = new MainSettingsFragment();
        
        getFragmentManager().beginTransaction()
            .add(R.id.container, mainSettings)
            .commit();
    }
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getFragmentManager().putFragment(outState, KEY_MAIN_SETTINGS_FRAGMENT, mainSettings);
	}


	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
	public boolean onPrepareOptionsMenu(Menu menu) {
    	startStopMenuItem = menu.findItem(R.id.action_start_stop);
    	onServiceStateChange(FilterService.isRunning());
		return super.onPrepareOptionsMenu(menu);
		
	}

	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.action_start_stop:
        		if (mainSettings.isFilterServiceStarted()) {
        			mainSettings.stopFilterService();
        			item.setTitle(getString(R.string.start));
        		}
        		else {
        			mainSettings.startFilterService();
        			item.setTitle(getString(R.string.stop));
        		}
        		return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

	@Override
	public void onServiceStateChange(boolean started) {
		int titleRes = started ? R.string.stop : R.string.start;
		int iconRes = started ? R.drawable.ic_action_pause : R.drawable.ic_action_play;
		String title = getString(titleRes);
		startStopMenuItem.setTitle(title);
		startStopMenuItem.setIcon(iconRes);
	}

}
