package com.steinerize.android.colortemperature.callbacks;

import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public abstract class OnSeekBarChangeListenerExtended implements OnSeekBarChangeListener {

	@Override
	public abstract void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser);

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {};

}
