package com.luxiliu.soccernewsaustralia.preference;

import android.app.ActionBar;
import android.os.Bundle;
import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.SNAActivity;

/**
 * The SettingsActivity is used to set and get application's preferences
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SettingsActivity extends SNAActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			setContentView(R.layout.settings_activity);

			setContent();
		}
	}

	private void setContent() {
		getActionBar().setDisplayOptions(
				ActionBar.DISPLAY_USE_LOGO | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_HOME_AS_UP
						| ActionBar.DISPLAY_SHOW_TITLE);

		setTitle(R.string.settings);
	}
}
