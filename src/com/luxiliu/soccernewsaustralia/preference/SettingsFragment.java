package com.luxiliu.soccernewsaustralia.preference;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.BrowserActivity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.MenuItem;

/**
 * The SettingsFragment provides a fragment of settings
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		// Enable "Home" menu
		setHasOptionsMenu(true);

		// Add preferences
		addPreferencesFromResource(R.xml.settings);

		// Setup preferences
		setupGeneralPreferences();
		setupAboutPreferences();
	}

	private void setupGeneralPreferences() {
		setupArticleTextSizePreference();
	}

	private void setupArticleTextSizePreference() {
		// Get the preference
		ListPreference preference = (ListPreference) findPreference(getString(R.string.article_text_size_preference_key));
		preference.setSummary(Preferences.getArticleFontSize(
				preference.getContext()).toString());
		preference
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
					@Override
					public boolean onPreferenceChange(Preference preference,
							Object newValue) {
						preference.setSummary(Preferences.toFontSize(
								preference.getContext(), (String) newValue)
								.toString());
						return true;
					}
				});
	}

	private void setupAboutPreferences() {
		setupOpenSourcePreference();
		setupTermsOfServicePreference();
		setupPrivacyPolicyPreference();
		setupAboutAppPreference();
	}

	private void setupOpenSourcePreference() {
		// Get the preference
		findPreference(getString(R.string.open_source_licenses_preference_key))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference arg0) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(getResources().getString(
								R.string.open_source_licenses_url)));
						intent.setClass(getActivity(), BrowserActivity.class);
						startActivity(intent);
						return true;
					}
				});
	}

	private void setupTermsOfServicePreference() {
		// Get the preference
		findPreference(getString(R.string.terms_of_service_preference_key))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference arg0) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(getResources().getString(
								R.string.terms_of_service_url)));
						startActivity(intent);
						return true;
					}
				});
	}

	private void setupPrivacyPolicyPreference() {
		// Get the preference
		findPreference(getString(R.string.privacy_policy_preference_key))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference arg0) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(getResources().getString(
								R.string.privacy_policy_url)));
						startActivity(intent);
						return true;
					}
				});
	}

	private void setupAboutAppPreference() {
		// Find the preference
		Preference preference = findPreference(getResources().getString(
				R.string.version_preference_key));

		try {
			// Set application name as title
			preference.setTitle(R.string.app_name);

			// Set version information as summary
			PackageInfo packageInfo = getActivity().getPackageManager()
					.getPackageInfo(getActivity().getPackageName(), 0);
			String versionName = packageInfo.versionName;
			int versionCode = packageInfo.versionCode;
			Object[] objects = new Object[2];
			objects[0] = versionName;
			objects[1] = Integer.valueOf(versionCode);
			String version = getString(R.string.release, objects);
			preference.setSummary(version);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			if (getActivity() != null) {
				getActivity().onBackPressed();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
