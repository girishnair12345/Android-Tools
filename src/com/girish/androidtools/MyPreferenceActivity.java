package com.girish.androidtools;

import com.girish.android.tools.CheckBoxListPreference;
import com.girish.android.tools.MutableCheckBoxListPreference;
import com.girish.android.tools.MutableListPreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;

public class MyPreferenceActivity extends PreferenceActivity implements OnPreferenceClickListener{
	
	MutableListPreference prefMutableListPreference;
	CheckBoxListPreference prefCheckBoxListPreference;
	MutableCheckBoxListPreference prefMutableCheckBoxListPreference;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.prefs);
		
		prefMutableListPreference = (MutableListPreference) findPreference("prefMutableListPreference");
		prefMutableListPreference.setOnPreferenceClickListener(this);
		
		prefCheckBoxListPreference = (CheckBoxListPreference) findPreference("prefCheckBoxListPreference");
		prefCheckBoxListPreference.setOnPreferenceClickListener(this);
		
		prefMutableCheckBoxListPreference = (MutableCheckBoxListPreference) findPreference("prefMutableCheckBoxListPreference");
		prefMutableCheckBoxListPreference.setOnPreferenceClickListener(this);
		
	}

	@Override
	public boolean onPreferenceClick(Preference arg0) {
		if(arg0.getKey().equals("prefMutableListPreference")){
			prefMutableListPreference.addEntry("key 1", "value 1");
			prefMutableListPreference.addEntry("key 2", "value 2");
			prefMutableListPreference.addEntry("key 3", "value 3");
			prefMutableListPreference.removeEntry("key 2");
		} else if(arg0.getKey().equals("prefMutableCheckBoxListPreference")){
			prefMutableCheckBoxListPreference.addEntry("key 1", "value 1",true);
			prefMutableCheckBoxListPreference.addEntry("key 2", "value 2",true);
			prefMutableCheckBoxListPreference.addEntry("key 3", "value 3",false);
			prefMutableCheckBoxListPreference.removeEntry("key 2");
		}
		
		return false;
	}

}
