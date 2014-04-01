package com.luxiliu.soccernewsaustralia.drawer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeActivity;
import com.luxiliu.soccernewsaustralia.home.HomePage;

/**
 * The HomePageEntry provides a drawer entry associated with a HomePage
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class HomePageEntry extends NavDrawerEntry {
	private HomePage mHomePage;

	public HomePageEntry(HomePage homePage) {
		mHomePage = homePage;
	}

	@Override
	public View getView(View view, ViewGroup viewGroup) {
		// Return the view of the entry
		Context context = viewGroup.getContext();
		TextView textView = (TextView) LayoutInflater.from(context).inflate(
				R.layout.nav_drawer_entry, null);

		// Use RobotoSlab font
		Typeface typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		textView.setTypeface(typeface);
		textView.setText(mHomePage.getTitle(context));

		return textView;
	}

	@Override
	public void onClick(View view) {
		// Send the intent to HomeActivity to update the home page
		Intent intent = new Intent();
		intent.setClass(view.getContext(), HomeActivity.class);
		intent.putExtra(Intent.EXTRA_UID, mHomePage.getId());
		view.getContext().startActivity(intent);
	}
}
