package com.luxiliu.soccernewsaustralia.activity;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.home.HomeActivity;
import com.luxiliu.soccernewsaustralia.net.ConnectionManager;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * The StartActivity is the launcher activity of the application
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class StartActivity extends SNAActivity {
	private static final String LOG_TAG = "StartActivity";
	private static final String NO_NETWORK_DIALOG_FRAGMENT = "no_network_dialog_fragment";

	private NoNetworkDialogFragment mNoNetworkDialogFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.start_activity);

		if (savedInstanceState == null) {
			// first time init, set initial content
			initContent();
		}
	}

	private void initContent() {
		if (ConnectionManager.instance().isConnected(this)) {
			// network is OK
			// start HomeActivity
			Intent intent = new Intent();
			intent.setClass(this, HomeActivity.class);
			startActivity(intent);
			finish();
		} else {
			// no network connection
			// show a dialog and confirm to close application
			showNoNetworkDialogFragment();
		}
	}

	private void showNoNetworkDialogFragment() {
		if (mNoNetworkDialogFragment == null) {
			// create a no-network-dialog-fragment if not created yet
			mNoNetworkDialogFragment = new NoNetworkDialogFragment();
		}

		if (!mNoNetworkDialogFragment.isVisible()) {
			try {
				// find a existing fragment
				FragmentManager fragmentManager = getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				Fragment fragment = fragmentManager
						.findFragmentByTag(NO_NETWORK_DIALOG_FRAGMENT);

				// remove the found fragment
				if (fragment != null) {
					fragmentTransaction.remove(fragment);
				}

				// add and show the dialog
				fragmentTransaction.addToBackStack(null);
				mNoNetworkDialogFragment.show(fragmentTransaction,
						NO_NETWORK_DIALOG_FRAGMENT);
			} catch (Throwable t) {
				Log.d(LOG_TAG, "Error showing dialog"
						+ NO_NETWORK_DIALOG_FRAGMENT);
			}
		}
	}

	public static class NoNetworkDialogFragment extends DialogFragment {

		private void finishHostActivity() {
			// finish the activity
			FragmentActivity fragmentActivity = getActivity();
			if (fragmentActivity != null) {
				fragmentActivity.finish();
			}
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			// create the dialog
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setTitle(R.string.no_network_connection);
			builder.setMessage(R.string.start_offline);
			return builder.create();
		}

		@Override
		public void onDismiss(DialogInterface dialog) {
			super.onDismiss(dialog);

			// finish the activity when the dialog dismissed
			finishHostActivity();
		}
	}
}