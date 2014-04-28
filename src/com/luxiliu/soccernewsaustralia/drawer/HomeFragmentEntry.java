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
import com.luxiliu.soccernewsaustralia.home.HomeFragment;

/**
 * The HomeFragmentEntry provides a drawer entry associated with a HomeFragment
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class HomeFragmentEntry extends NavDrawerEntry {
	private HomeFragment mHomeFragment;
	private TextView mTextView;
	private boolean mActivated;

	public HomeFragmentEntry(HomeFragment homeFragment) {
		mHomeFragment = homeFragment;
	}

	@Override
	public View getView(View view, ViewGroup viewGroup) {
		// get the context of the view group
		Context context = viewGroup.getContext();

		if (mTextView == null) {
			// setup the view for the first time
			mTextView = (TextView) LayoutInflater.from(context).inflate(
					R.layout.nav_drawer_entry, null);
			mTextView.setText(mHomeFragment.getTitle(context));
		} else {
			// the view is already initialized
		}

		// set activated/deactivated state
		setInternalActivated(context, mActivated);

		return mTextView;
	}

	@Override
	public void onClick(View view) {
		// inform HomeActivity to update the home fragment when it's clicked
		Intent intent = new Intent();
		intent.setClass(view.getContext(), HomeActivity.class);
		intent.putExtra(Intent.EXTRA_UID, mHomeFragment.getInternalId());
		view.getContext().startActivity(intent);
	}

	public void setActivated(Context context, boolean activated) {
		// keep view activated/deactivated state
		mActivated = activated;

		// set activated/deactivated state
		if (mTextView != null) {
			setInternalActivated(context, mActivated);
		}
	}

	private void setInternalActivated(Context context, boolean activated) {
		if (activated) {
			// set activated type face
			Typeface typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/Roboto-Bold.ttf");
			mTextView.setTypeface(typeface);
		} else {
			// set deactivated type face
			Typeface typeface = Typeface.createFromAsset(context.getAssets(),
					"fonts/Roboto-Light.ttf");
			mTextView.setTypeface(typeface);
		}
	}
}
