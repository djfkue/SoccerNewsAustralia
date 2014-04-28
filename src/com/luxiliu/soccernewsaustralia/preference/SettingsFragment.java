package com.luxiliu.soccernewsaustralia.preference;

import com.luxiliu.soccernewsaustralia.R;
import com.luxiliu.soccernewsaustralia.activity.BrowserActivity;
import com.luxiliu.soccernewsaustralia.util.Util;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceFragment;
import android.preference.Preference.OnPreferenceClickListener;
import android.view.MenuItem;

/**
 * The SettingsFragment is the fragment of preferences settings
 * 
 * @author Luxi Liu (luxi.liu@gmail.com)
 * 
 */
public class SettingsFragment extends PreferenceFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// enable "Home" menu
		setHasOptionsMenu(true);

		// add preferences
		addPreferencesFromResource(R.xml.settings);

		// setup preferences
		setupGeneralPreferences();
		setupAboutPreferences();
	}

	private void setupGeneralPreferences() {
		setupArticleTextSizePreference();
	}

	private void setupArticleTextSizePreference() {
		// get the preference
		ListPreference preference = (ListPreference) findPreference(getString(R.string.article_text_size_preference_key));

		// set preference summary and listener
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
		setupGoalPrivacyPolicyPreference();
		setupFfaPrivacyPolicyPreference();
		setupAboutAppPreference();
	}

	private void setupOpenSourcePreference() {
		// get the preference and setup listener
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
		// get the preference and setup listener
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

	private void setupGoalPrivacyPolicyPreference() {
		// get the preference and setup listener
		findPreference(getString(R.string.goal_privacy_policy_preference_key))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference arg0) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(getResources().getString(
								R.string.goal_privacy_policy_url)));
						startActivity(intent);
						return true;
					}
				});
	}

	private void setupFfaPrivacyPolicyPreference() {
		// get the preference and setup listener
		findPreference(getString(R.string.ffa_privacy_policy_preference_key))
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference arg0) {
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(getResources().getString(
								R.string.ffa_privacy_policy_url)));
						startActivity(intent);
						return true;
					}
				});
	}

	private void setupAboutAppPreference() {
		// get the preference
		Preference preference = findPreference(getResources().getString(
				R.string.version_preference_key));

		// set application name as title
		preference.setTitle(R.string.app_name);

		// set version information as summary
		preference.setSummary(Util.getApplicationVersion(getActivity()));
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
