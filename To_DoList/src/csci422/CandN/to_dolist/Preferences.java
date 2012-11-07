/*
 * Chris Card
 * Nathan Harvey
 * 11/6/12
 * This class allows for preferences to be selected 
 */
package csci422.CandN.to_dolist;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

public class Preferences extends PreferenceActivity {

	private SharedPreferences prefs = null;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		addPreferencesFromResource(R.xml.preferences);
	}

	@Override 
	public void onResume()
	{
		super.onResume();

		prefs = PreferenceManager.getDefaultSharedPreferences(this);
	}
	@Override 
	public void onPause()
	{
		super.onPause();
	}
}
