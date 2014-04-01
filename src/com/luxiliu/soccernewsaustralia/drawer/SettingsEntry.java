package com.luxiliu.soccernewsaustralia.drawer;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.preference.SettingsActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * The SettingsEntry provides a drawer entry associated with SettingsActivity
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SettingsEntry extends NavDrawerEntry {

	@Override
	public View getView(View view, ViewGroup viewGroup) {
		// Get context
		Context context = viewGroup.getContext();

		// Inflate main view
		LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context)
				.inflate(R.layout.nav_drawer_misc_entry, null);

		// Set the text and the image
		TextView textView = (TextView) linearLayout
				.findViewById(R.id.entry_label);
		textView.setText(context.getString(R.string.settings).toUpperCase(
				context.getResources().getConfiguration().locale));
		if (Build.VERSION.SDK_INT > 17) {
			textView.setCompoundDrawablesRelativeWithIntrinsicBounds(
					R.drawable.ic_gear_40, 0, 0, 0);
		}

		// Set divider
		linearLayout.findViewById(R.id.lower_divider).setVisibility(
				View.VISIBLE);

		return linearLayout;
	}

	@Override
	public void onClick(View view) {
		// Start SettingsActivity
		Intent intent = new Intent();
		intent.setClass(view.getContext(), SettingsActivity.class);
		view.getContext().startActivity(intent);
	}
}
