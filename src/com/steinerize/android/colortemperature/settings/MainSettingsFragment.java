package com.steinerize.android.colortemperature.settings;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.steinerize.android.colortemperature.FilterService;
import com.steinerize.android.colortemperature.R;
import com.steinerize.android.colortemperature.callbacks.OnSeekBarChangeListenerExtended;
import com.steinerize.android.colortemperature.callbacks.ServiceStateChangeListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainSettingsFragment extends Fragment {
	
	private static final String PREFS_COLOR = "PREFS_COLOR";
	
	private int opacity;
	private int colorTemp;
	private int darkness;
	
	private ServiceStateChangeListener serviceStateChangeListener; 
	
    public MainSettingsFragment() {
    }
    
    @Override
	public void onPause() {
		super.onPause();
		save();
	}
    
    
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			serviceStateChangeListener = (ServiceStateChangeListener) activity;
		}
		catch(ClassCastException e) {
			throw new ClassCastException(activity.toString() + " must implement " + 
				ServiceStateChangeListener.class.toString());
		}
	}

	public void startFilterService() {
    	Activity activity = getActivity();
    	Intent intent = new Intent(activity, FilterService.class);
    	
    	intent.putExtra(Keys.OPACITY, opacity);
    	intent.putExtra(Keys.COLOR_TEMP, colorTemp * Defaults.COLOR_TEMP_FACTOR);
    	intent.putExtra(Keys.DARKNESS, darkness);
    	
    	activity.startService(intent);
    	serviceStateChangeListener.onServiceStateChange(true);
    }
	
	public boolean isFilterServiceStarted() {
		return FilterService.isRunning();
	}
	
	public void stopFilterService() {
		Activity activity = getActivity();
		activity.stopService(new Intent(activity, FilterService.class));
		serviceStateChangeListener.onServiceStateChange(false);
	}
    
    private void load() {
    	Activity activity = getActivity();
    	SharedPreferences settings = 
    		activity.getSharedPreferences(PREFS_COLOR, Context.MODE_PRIVATE);
    	
    	opacity = settings.getInt(Keys.OPACITY, Defaults.OPACITY);
    	colorTemp = settings.getInt(Keys.COLOR_TEMP, 
    		Defaults.COLOR_TEMP / Defaults.COLOR_TEMP_FACTOR);
    	darkness = settings.getInt(Keys.DARKNESS, Defaults.DARKNESS);
    }
    
    private void save() {
    	Activity activity = getActivity();
    	SharedPreferences settings = 
    		activity.getSharedPreferences(PREFS_COLOR, Context.MODE_PRIVATE);
    	SharedPreferences.Editor editor = settings.edit();
    	
    	editor.putInt(Keys.OPACITY, opacity);
    	editor.putInt(Keys.COLOR_TEMP, colorTemp);
    	editor.putInt(Keys.DARKNESS, darkness);
    	
    	editor.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        
        final SeekBar opacitySeekBar = (SeekBar) rootView.findViewById(R.id.opacitySeekBar);
        final SeekBar colorTempSeekBar = (SeekBar) 
        		rootView.findViewById(R.id.colorTemperatureSeekBar);
        final SeekBar darknessSeekBar = (SeekBar) rootView.findViewById(R.id.darknessSeekBar);
        
        load();
        
        opacitySeekBar.setProgress(opacity);
        colorTempSeekBar.setProgress(colorTemp);
        darknessSeekBar.setProgress(darkness);
        
        opacitySeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				opacity = progress;
				startFilterService();
			}
		});
        
        colorTempSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				colorTemp = progress;
				startFilterService();
			}
		});
        
        darknessSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListenerExtended() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
				darkness = progress;
				startFilterService();
			}
		});
        
        return rootView;
    }
}